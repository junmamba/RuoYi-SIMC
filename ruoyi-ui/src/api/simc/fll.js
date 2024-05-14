import request from '@/utils/request'

// 查询表格列表
export function tableList(query) {
  return request({
    url: '/simc/fll/list',
    method: 'get',
    params: query
  })
}
