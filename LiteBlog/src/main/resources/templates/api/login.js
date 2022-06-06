function loginApi(data) {
  return $axios({
    'url': '/users/login',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/users/logout',
    'method': 'post',
  })
}
