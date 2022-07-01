// 删除接口
const deleteBlog = (id) => {
    return $axios({
        url: '/blogs/' + id,
        method: 'delete',
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