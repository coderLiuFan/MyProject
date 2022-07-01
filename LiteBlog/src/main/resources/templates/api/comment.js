const saveComment = (params) =>{
    return $axios({
        url: '/comments',
        method: 'post',
        params
    })
}

const deleteComment = (id) => {
    return $axios({
        url: '/comments/' + id,
        method: 'delete',
    })
}