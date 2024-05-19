import request from '@/utils/request'

export function listChaiSangDistrict(query) {
  return request({
    url: '/simc/district/listChaiSangDistrict',
    method: 'get'
  });
}

// 查询行政区域列表
export function listDistrict(query) {
  return request({
    url: '/simc/district/list',
    method: 'get',
    params: query
  })
}

export function listDistrictExcludeChild(districtId) {
  return request({
    url: '/simc/district/list/exclude/' + districtId,
    method: 'get'
  })
}

// 查询行政区域详细
export function getDistrict(districtId) {
  return request({
    url: '/simc/district/getDistrictByDistrictId/' + districtId,
    method: 'get'
  })
}

// 新增行政区域
export function addDistrict(data) {
  return request({
    url: '/simc/district',
    method: 'post',
    data: data
  })
}

// 修改行政区域
export function updateDistrict(data) {
  return request({
    url: '/simc/district',
    method: 'put',
    data: data
  })
}

// 删除行政区域
export function delDistrict(districtId) {
  return request({
    url: '/simc/district/' + districtId,
    method: 'delete'
  })
}
