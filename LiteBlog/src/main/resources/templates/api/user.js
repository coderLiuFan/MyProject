const updateUser = (params) => {
    return $axios({
        url: '/users',
        method: 'put',
        data: {...params}
    })
}