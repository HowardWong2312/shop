package io.dubai.common.utils;

public class ResultsCode {

    public static final int ACCOUNT_IS_NOT_FOUND = 101;
    public static final int ACCOUNT_PASSWORD_IS_NOT_SET = 102;
    public static final int TRANSACTION_PASSWORD_ERROR = 103;
    public static final int TRANSACTION_AMOUNT_IS_NOT_ENOUGH = 104;
    public static final int TRANSACTION_IS_NOT_FUND = 105;
    public static final int TRANSACTION_FIRST = 201;


    //统一参数不足
    public static final int PARAMS_IS_NOT_FONT = 302;

    //当需要返回错误，但又不需要让用户重新登录的时候
    public static final int ERROR_BUT_NOT_RE_LOGIN = 402;


    //参数不正确
    public static final int PARAMS_IS_ERROR = 502;
}
