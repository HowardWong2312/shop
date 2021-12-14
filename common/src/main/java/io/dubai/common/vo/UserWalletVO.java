package io.dubai.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserWalletVO extends JsonResultVO {

    private List<WalletAssetsVO> list;

}
