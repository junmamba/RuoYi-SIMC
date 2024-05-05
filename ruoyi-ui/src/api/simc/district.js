import request from '@/utils/request'

export function listChaiSangDistrict(query) {
  return request({
    url: '/simc/district/listChaiSangDistrict',
    method: 'get'
  });
}
