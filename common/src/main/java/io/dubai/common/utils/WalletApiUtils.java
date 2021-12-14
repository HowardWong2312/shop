package io.dubai.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dubai.common.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletApiUtils {

    private final static Logger logger = LoggerFactory.getLogger(WalletApiUtils.class);

    // region 获取用户钱包指定币种的余额
    public static SymbolBalanceVO getUserBalanceBySymbol(String url, Long userId, String symbol) {
        SymbolBalanceVO symbolBalanceVO = new SymbolBalanceVO();
        symbolBalanceVO.setAvailable(BigDecimal.ZERO);
        symbolBalanceVO.setFreeze(BigDecimal.ZERO);
        symbolBalanceVO.setSymbol(symbol);
        if (null == url || null == userId || null == symbol) {
            return symbolBalanceVO;
        }
        UserWalletVO userWalletVO = getUserWallet(url, userId);
        if (userWalletVO.getStatus()) {
            List<WalletAssetsVO> list = userWalletVO.getList();
            Map<String, WalletAssetsVO> walletAssetsMap = new HashMap<>();
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    WalletAssetsVO walletAssetsVO = list.get(i);
                    walletAssetsMap.put(walletAssetsVO.getCoinName(), walletAssetsVO);
                }
            }

            WalletAssetsVO walletAssetsVO = walletAssetsMap.get(symbol);
            // region 计算钱包币种余额，要合并usdt和omni_usdt
            if (null == walletAssetsVO) {
                symbolBalanceVO.setAvailable(BigDecimal.ZERO);
                symbolBalanceVO.setFreeze(BigDecimal.ZERO);
                symbolBalanceVO.setSymbol(symbol);
            } else {
                if ("USDT".equalsIgnoreCase(symbol)) {
                    WalletAssetsVO omni = walletAssetsMap.get("Omni_USDT");
                    symbolBalanceVO.setAvailable(null == omni ? walletAssetsVO.getBalance() : walletAssetsVO.getBalance().add(omni.getBalance()));
                    symbolBalanceVO.setFreeze(walletAssetsVO.getFrozenBalance().add(null == omni ? BigDecimal.ZERO : omni.getFrozenBalance()));
                } else {
                    symbolBalanceVO.setAvailable(walletAssetsVO.getBalance());
                    symbolBalanceVO.setFreeze(walletAssetsVO.getFrozenBalance());
                }
            }
            // endregion
            return symbolBalanceVO;
        }
        return symbolBalanceVO;
    }
    // endregion

    // region 获取用户钱包余额、充币地址
    public static UserWalletVO getUserWallet(String url, Long userId) {
        UserWalletVO userWalletVO = new UserWalletVO();
        if (null == url || null == userId) {
            userWalletVO.setStatus(false);
            userWalletVO.setMessage("参数为空");
            userWalletVO.setList(null);
            userWalletVO.setCode(500);
            return userWalletVO;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId.toString());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        try {
            String str = HttpUtils.sendPostJson(url, null, jsonObject.toJSONString());
            JSONObject res = JSONObject.parseObject(str);
            if (null != res) {
                res = res.getJSONObject("data");
            }
            boolean status = res.getBooleanValue("Status");
            String error = res.getString("Message");
            Integer code = res.getInteger("Code");
            if (status) {
                JSONArray data = res.getJSONArray("Data");
                if (null == data || data.size() == 0) {
                    userWalletVO.setStatus(false);
                    userWalletVO.setMessage("用户无钱包地址");
                    userWalletVO.setList(null);
                    userWalletVO.setCode(code);
                    return userWalletVO;
                }

                List<WalletAssetsVO> list = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    JSONObject tmp = data.getJSONObject(i);
                    WalletAssetsVO walletAssetsVO = new WalletAssetsVO();
                    walletAssetsVO.setAddress(tmp.getString("Address"));
                    walletAssetsVO.setBalance(tmp.getBigDecimal("Balance"));
                    walletAssetsVO.setCoinName(tmp.getString("CoinName"));
                    walletAssetsVO.setFrozenBalance(tmp.getBigDecimal("FrozenBalance"));
                    walletAssetsVO.setId(tmp.getLong("Id"));
                    walletAssetsVO.setIsLock(tmp.getInteger("IsLock"));
                    walletAssetsVO.setUserId(tmp.getInteger("MemberId"));
                    walletAssetsVO.setToReleased(tmp.getInteger("ToReleased"));
                    walletAssetsVO.setType(tmp.getString("Type"));
                    walletAssetsVO.setVersion(tmp.getLong("Version"));
                    list.add(walletAssetsVO);
                }

                userWalletVO.setMessage(error);
                userWalletVO.setStatus(true);
                userWalletVO.setCode(code);
                userWalletVO.setList(list);

                return userWalletVO;
            } else {
                logger.error("get user wallet error: code:{}，msg:{}, params:{}", code, error, userId.toString());
                userWalletVO.setStatus(false);
                userWalletVO.setMessage(error);
                userWalletVO.setList(null);
                userWalletVO.setCode(500);
                return userWalletVO;
            }
        } catch (Exception e) {
            logger.error("调用钱包api接口url:{}失败", url);
            logger.error("接口调用失败:" + e);
            userWalletVO.setStatus(false);
            userWalletVO.setMessage("获取用户钱包接口调用失败");
            userWalletVO.setList(null);
            userWalletVO.setCode(500);
            return userWalletVO;
        }
    }
    // endregion

    // region 钱包转账
    public static JsonResultVO transfer(String url, Long userId, String coinName, BigDecimal amount) {
        JsonResultVO jsonResultVO = new JsonResultVO();
        if (null == url || null == userId || null == coinName || null == amount) {
            jsonResultVO.setCode(500);
            jsonResultVO.setStatus(false);
            jsonResultVO.setMessage("请求参数为空");
            return jsonResultVO;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId.toString());
        jsonObject.put("amount", amount.toString());
        jsonObject.put("coinName", coinName);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        try {
            String str = HttpUtils.sendPostJson(url, null, jsonObject.toJSONString());
            JSONObject res = JSONObject.parseObject(str);
            if (null != res) {
                res = res.getJSONObject("data");
            }
            boolean status = res.getBooleanValue("Status");
            String error = res.getString("Message");
            Integer code = res.getInteger("Code");
            JSONObject data = res.getJSONObject("Data");
            if (status) {
                jsonResultVO.setMessage(error);
                jsonResultVO.setStatus(true);
                jsonResultVO.setCode(code);
                return jsonResultVO;
            } else {
                logger.error("user wallet assets transfer error: code:{}，msg:{}, params:{}", code, error, jsonObject.toString());
                jsonResultVO.setStatus(false);
                jsonResultVO.setMessage(error);
                jsonResultVO.setCode(500);
                return jsonResultVO;
            }
        } catch (Exception e) {
            logger.error("调用钱包api接口url:{}失败", url);
            logger.error("用户钱包接口调用失败:" + e);
            jsonResultVO.setStatus(false);
            jsonResultVO.setMessage("用户钱包接口调用失败");
            jsonResultVO.setCode(500);
            return jsonResultVO;
        }
    }
    // endregion

    public static void main(String[] args) {
        String url = "https://www.athensmining.com/gateway/api/v1/wallet/generateAddr/";
        JsonResultVO json = createWalletAddress(url,1111L);
        System.out.println(json.toString());
    }

    // region 创建用户钱包地址
    public static JsonResultVO createWalletAddress(String url, Long userId) {
        JsonResultVO jsonResultVO = new JsonResultVO();
        if (null == url || null == userId) {
            jsonResultVO.setCode(500);
            jsonResultVO.setStatus(false);
            jsonResultVO.setMessage("请求参数为空");
            return jsonResultVO;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId.toString());

        try {
            String str = HttpUtils.sendPostJson(url, null, jsonObject.toJSONString());
            JSONObject res = JSONObject.parseObject(str);
            if (null != res) {
                res = res.getJSONObject("data");
            }
            boolean status = res.getBooleanValue("Status");
            String error = res.getString("Message");
            Integer code = res.getInteger("Code");
            if (status) {
                jsonResultVO.setMessage(error);
                jsonResultVO.setStatus(true);
                jsonResultVO.setCode(code);
                JSONArray jsonArray = res.getJSONArray("Data");
                jsonResultVO.setData(jsonArray.getJSONObject(0));
                return jsonResultVO;
            } else {
                logger.error("create user wallet address error: code:{}，msg:{}, params:{}", code, error, jsonObject.toString());
                jsonResultVO.setStatus(false);
                jsonResultVO.setMessage(error);
                jsonResultVO.setCode(500);
                return jsonResultVO;
            }
        } catch (Exception e) {
            logger.error("调用钱包api接口url:{}失败", url);
            logger.error("用户钱包接口调用失败:" + e);
            jsonResultVO.setStatus(false);
            jsonResultVO.setMessage("用户钱包接口调用失败");
            jsonResultVO.setCode(500);
            return jsonResultVO;
        }
    }
    // endregion

    // region 根据币种名称、充币地址分页获取充币历史
    public static IPage<WalletRechargeVO> getRechargeRecord(String url, String coinName, String address, Integer pageNo, Integer pageSize) {
        IPage<WalletRechargeVO> page = new Page<>(pageNo,pageSize);
        page.setTotal(0);
        page.setPages(0);

        if (null == url || null == coinName || null == address) {
            return page;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coinName", coinName);
        jsonObject.put("address", address);
        jsonObject.put("currentpage", pageNo.toString());
        jsonObject.put("pagesize", pageSize.toString());

        try {
            String str = HttpUtils.sendPostJson(url, null, jsonObject.toJSONString());
            JSONObject res = JSONObject.parseObject(str);
            if (null != res) {
                res = res.getJSONObject("data");
            }
            boolean status = res.getBooleanValue("Status");
            String error = res.getString("Message");
            Integer code = res.getInteger("Code");
            if (status) {
                JSONObject data = res.getJSONObject("Data");
                if (null == data) {
                    return page;
                }

                Integer totalSize = data.getInteger("TotalItem");
                Integer totalPages = data.getInteger("Totalpages");

                page.setTotal(totalSize);
                page.setPages(totalPages);

                JSONArray records = data.getJSONArray("Data");

                List<WalletRechargeVO> list = new ArrayList<>();
                if (null != records && records.size() > 0) {
                    for (int i = 0; i < records.size(); i++) {
                        JSONObject tmp = records.getJSONObject(i);
                        WalletRechargeVO walletRechargeVO = new WalletRechargeVO();
                        walletRechargeVO.setAddress(tmp.getString("Address"));
                        walletRechargeVO.setAmount(tmp.getBigDecimal("Amount"));
                        String symbol = tmp.getString("CoinName");
                        walletRechargeVO.setCoinName(symbol.equalsIgnoreCase("Omni_USDT") ? "USDT" : symbol);
                        walletRechargeVO.setConfirmHeight(tmp.getString("ConfirmHeight"));
                        walletRechargeVO.setId(tmp.getLong("Id"));
                        walletRechargeVO.setTxHeight(tmp.getString("TxHeight"));
                        walletRechargeVO.setTxHash(tmp.getString("TxHash"));
                        walletRechargeVO.setTime(tmp.getLong("Time") * 1000);
                        walletRechargeVO.setState(tmp.getInteger("State"));
                        list.add(walletRechargeVO);
                    }
                }
                page.setRecords(list);
                return page;
            } else {
                logger.error("get user wallet recharge records error: code:{}，msg:{}", code, error);
                return page;
            }
        } catch (Exception e) {
            logger.error("调用钱包api接口url:{}失败", url);
            logger.error("接口调用失败:" + e);
            return page;
        }
    }
    // endregion

    // region 根据用户账号、币种分页获取提币历史
    public static IPage<WalletWithdrawVO> getWithdrawRecord(String url, String coinName, Long userId, Integer pageNo, Integer pageSize) {
        IPage<WalletWithdrawVO> page = new Page<>(pageNo,pageSize);
        page.setTotal(0);
        page.setPages(0);

        if (null == url || null == coinName || null == userId) {
            return page;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coinName", coinName);
        jsonObject.put("userId", userId.toString());
        jsonObject.put("currentpage", pageNo.toString());
        jsonObject.put("pagesize", pageSize.toString());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        try {
            String str = HttpUtils.sendPostJson(url, null, jsonObject.toJSONString());
            JSONObject res = JSONObject.parseObject(str);
            if (null != res) {
                res = res.getJSONObject("data");
            }
            boolean status = res.getBooleanValue("Status");
            String error = res.getString("Message");
            Integer code = res.getInteger("Code");
            if (status) {
                JSONObject data = res.getJSONObject("Data");
                if (null == data) {
                    return page;
                }

                Integer totalSize = data.getInteger("TotalItem");
                Integer totalPages = data.getInteger("Totalpages");

                page.setTotal(totalSize);
                page.setPages(totalPages);

                JSONArray records = data.getJSONArray("Data");

                List<WalletWithdrawVO> list = new ArrayList<>();
                if (null != records && records.size() > 0) {
                    for (int i = 0; i < records.size(); i++) {
                        JSONObject tmp = records.getJSONObject(i);
                        WalletWithdrawVO walletWithdrawVO = new WalletWithdrawVO();
                        walletWithdrawVO.setAddress(tmp.getString("Address"));
                        walletWithdrawVO.setAmount(tmp.getBigDecimal("Amount"));
                        walletWithdrawVO.setCoinName(tmp.getString("CoinName"));
                        walletWithdrawVO.setId(tmp.getLong("Id"));
                        walletWithdrawVO.setTxHash(tmp.getString("TxHash"));
                        walletWithdrawVO.setTime(tmp.getLong("Time") * 1000);
                        walletWithdrawVO.setMemberId(tmp.getInteger("MemberId"));
                        list.add(walletWithdrawVO);
                    }
                }
                page.setRecords(list);
                return page;
            } else {
                logger.error("get user wallet withdraw records error: code:{}，msg:{}", code, error);
                return page;
            }
        } catch (Exception e) {
            logger.error("调用钱包api接口url:{}失败", url);
            logger.error("接口调用失败:" + e);
            return page;
        }
    }
    // endregion

}
