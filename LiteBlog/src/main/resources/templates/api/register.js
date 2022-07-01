const saveUser = (params) => {
    return $axios({
        url: '/users',
        method: 'post',
        params
    })
}