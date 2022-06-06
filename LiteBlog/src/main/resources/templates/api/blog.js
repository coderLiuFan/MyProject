// 删除接口
const deleteBlog = (id) => {
    return $axios({
        url: '/blogs/' + id,
        method: 'delete',
    })
}

const getBlogPage = (params) => {
    return $axios({
        url: '/blogs/page',
        method: 'get',
        params
    })
}

const saveBlog = (params) => {
    return $axios({
        url: '/blogs',
        method: 'post',
        params
    })
}

const updateBlog = (params) => {
    return $axios({
        url: '/blogs',
        method: 'put',
        data: {...params}
    })
}
// 不能回显数据，不能正常封装
// const queryBlog = (id) => {
//     return $axios({
//         url: '/blogs/' + id,
//         method: 'get',
//     })
// }

// function queryBlog(id) {
//     return $axios({
//         url: '/blogs/' + id,
//         method: 'get',
//     })
// }