package com.lite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lite.common.R;
import com.lite.dto.CommentDto;
import com.lite.entity.Comment;
import com.lite.entity.User;
import com.lite.service.CommentService;
import com.lite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    /**
     * 根据博客id查询其对应的评论，其中包括了每条评论作者的部分信息
     * 用于每篇博客详情页评论动态展示模块（active-comment.html）
     *
     * @param blogId 博客id
     * @return 对应的评论集合
     */
    @GetMapping("/getByBlogId/{blogId}")
    public R<List<CommentDto>> getByBlogId(@PathVariable Long blogId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentBlogId, blogId).orderByDesc(Comment::getCreateTime);
        List<CommentDto> commentDtoList = getCommentDtoByCondition(queryWrapper);
        return R.success(commentDtoList);
    }

    /**
     * 保存评论
     * 用于每条博客展示页面用户提交评论（blog-detail.html)
     * @param comment 评论数据
     * @param request 用于获取session，读取当前登录用户的id
     * @return 操作结果信息
     */
    @PostMapping
    public R<String> save(Comment comment, HttpServletRequest request) {
        log.info("保存评论");
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId != null) {
            comment.setCommentAuthorId(userId);
            boolean flag = commentService.save(comment);
            if (flag) {
                return R.success("评论成功！");
            }
        }
        return R.error("服务器开小差了，请稍后再试~");
    }

    /**
     * 删除评论
     * @param commentId 评论id
     * @return 操作结果信息
     */
    @DeleteMapping("/{commentId}")
    public R<String> delete(@PathVariable Long commentId) {
        log.info("开始删除评论，id={}",commentId);
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentId,commentId);
        boolean flag = commentService.remove(queryWrapper);
        if(flag){
            return R.success("删除评论成功");
        }
        return R.error("服务器开小差了，请稍后再试~");
    }

    /**
     * 获取评论列表Dto封装类，在controller中调用
     *
     * @param queryComment 查询评论的条件
     * @return commentDto集合
     */
    public List<CommentDto> getCommentDtoByCondition(LambdaQueryWrapper<Comment> queryComment) {
        // 依据参数条件获取到所有评论对象
        List<Comment> commentList = commentService.list(queryComment);
        return commentList.stream().map((item) -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(item, commentDto);
            try {
                // 查询该条评论作者
                LambdaQueryWrapper<User> queryUser = new LambdaQueryWrapper<>();
                queryUser.eq(User::getUserId, item.getCommentAuthorId());
                // 将用户昵称和用户头像封装进commentDto
                User user = userService.getOne(queryUser);
                commentDto.setAuthorName(user.getNickname());
                commentDto.setAuthorProfile(user.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return commentDto;
        }).collect(Collectors.toList());
    }
}
