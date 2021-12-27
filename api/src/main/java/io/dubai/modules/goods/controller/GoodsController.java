package io.dubai.modules.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.Login;
import io.dubai.common.annotation.LoginUser;
import io.dubai.common.enums.ResponseStatusEnum;
import io.dubai.common.enums.UserBalanceLogStatusEnum;
import io.dubai.common.enums.LogTypeEnum;
import io.dubai.common.enums.UserCreditsLogStatusEnum;
import io.dubai.common.exception.RRException;
import io.dubai.common.sys.service.IMessageService;
import io.dubai.common.sys.service.SysConfigService;
import io.dubai.common.utils.OssUtils;
import io.dubai.common.utils.R;
import io.dubai.common.utils.StringUtils;
import io.dubai.modules.goods.entity.*;
import io.dubai.modules.goods.entity.vo.GoodsGroupRecordVo;
import io.dubai.modules.goods.entity.vo.GoodsVo;
import io.dubai.modules.goods.entity.vo.PrizeVo;
import io.dubai.modules.goods.form.*;
import io.dubai.modules.goods.query.GoodsQuery;
import io.dubai.modules.goods.service.*;
import io.dubai.modules.other.entity.Language;
import io.dubai.modules.other.service.LanguageService;
import io.dubai.modules.user.entity.UserAddress;
import io.dubai.modules.user.entity.UserBalanceLog;
import io.dubai.modules.user.entity.UserCreditsLog;
import io.dubai.modules.user.service.UserAddressService;
import io.dubai.modules.user.service.UserBalanceLogService;
import io.dubai.modules.user.service.UserCreditsLogService;
import io.dubai.modules.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 算力产品
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@RestController
@RequestMapping("goods")
@Api(tags = "商品")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @Resource
    private LanguageService languageService;

    @Resource
    private GoodsImgService goodsImgService;

    @Resource
    private GoodsLangService goodsLangService;

    @Resource
    private GoodsCategoryService goodsCategoryService;

    @Resource
    private GoodsGroupService goodsGroupService;

    @Resource
    private GoodsGroupRecordService goodsGroupRecordService;

    @Resource
    private GoodsOrderService goodsOrderService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserBalanceLogService userBalanceLogService;

    @Resource
    private UserFavoriteGoodsService userFavoriteGoodsService;

    @Resource
    private UserCreditsLogService userCreditsLogService;

    @Resource
    private UserAddressService userAddressService;

    @Resource
    private GoodsOneBuyService goodsOneBuyService;

    @Resource
    private GoodsRushService goodsRushService;

    @Resource
    private IMessageService messageService;

    @Resource
    private SysConfigService sysConfigService;

    @ApiOperation("商品列表")
    @PostMapping("/list")
    @Login
    public R list(@RequestBody GoodsQuery query, @ApiIgnore @LoginUser UserInfo userInfo) {
        if(userInfo != null && userInfo.getCountryId() != null){
            query.setCountryId(userInfo.getCountryId().longValue());
        }
        Long languageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            languageId = language.getId();
        }
        query.setLanguageId(languageId);
        if(query.getCategoryId() != null){
            List<Long> ids = goodsCategoryService.queryIterativeIdsByParentId(query.getCategoryId());
            ids.add(query.getCategoryId());
            query.setCategoryIds(ids);
        }
        return R.ok().put("page", goodsService.queryPage(query));
    }


    @ApiOperation("根据主键查商品详情")
    @GetMapping("/info/{id}")
    @Login
    public R info(@PathVariable("id") Long id,@ApiIgnore @LoginUser UserInfo userInfo) {
        Long languageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            languageId = language.getId();
        }
        GoodsVo goodsVo = goodsService.queryInfoByIdAndLanguageId(id,languageId);
        if(goodsVo == null){
            throw new RRException(ResponseStatusEnum.PARAMS_IS_ERROR);
        }
        UserFavoriteGoods userFavoriteGoods = userFavoriteGoodsService.getOne(
                new QueryWrapper<UserFavoriteGoods>()
                .eq("user_id",userInfo.getUserId())
                .eq("goods_id",goodsVo.getId())
        );
        if(userFavoriteGoods != null){
            goodsVo.setIsFavorite(1);
        }
        List<GoodsImg> bannerList = goodsImgService.list(
                new QueryWrapper<GoodsImg>()
                        .eq("goods_id",id)
                        .eq("language_id",languageId)
                        .eq("type",1)
                        .orderByDesc("order_num")
        );

        List<GoodsImg> descImgList = goodsImgService.list(
                new QueryWrapper<GoodsImg>()
                        .eq("goods_id",id)
                        .eq("language_id",languageId)
                        .eq("type",2)
                        .orderByDesc("order_num")
        );
        goodsVo.setBannerList(bannerList);
        goodsVo.setDescImgList(descImgList);
        if(goodsVo.getIsGroup() == 1){
            GoodsGroup goodsGroup = goodsGroupService.getOne(
                    new QueryWrapper<GoodsGroup>()
                    .eq("goods_id",goodsVo.getId())
                    .eq("status",1)
            );
            if(goodsGroup != null){
                goodsVo.setGoodsGroup(goodsGroup);
            }
            List<GoodsGroupRecordVo> list = goodsGroupRecordService.queryListByGoodsId(goodsVo.getId(),2);
            if(list != null){
                goodsVo.setGoodsGroupRecordList(list);
            }
        }
        return R.ok().put("goods", goodsVo);
    }

    @Login
    @ApiOperation("根据主键和语言ID查商品详情")
    @GetMapping("/goodsInfo/{id}/{languageId}")
    public R goodsInfo(@PathVariable Long id, @PathVariable Long languageId,@ApiIgnore @LoginUser UserInfo userInfo) {
        Long defaultLanguageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            defaultLanguageId = language.getId();
        }
        GoodsVo goodsVo = goodsService.queryInfoByIdAndLanguageId(id,languageId);
        List<GoodsImg> bannerList = goodsImgService.list(
                new QueryWrapper<GoodsImg>()
                        .eq("goods_id",id)
                        .eq("language_id",languageId)
                        .eq("type",1)
                        .orderByDesc("order_num")
        );

        List<GoodsImg> descImgList = goodsImgService.list(
                new QueryWrapper<GoodsImg>()
                        .eq("goods_id",id)
                        .eq("language_id",languageId)
                        .eq("type",2)
                        .orderByDesc("order_num")
        );
        goodsVo.setBannerList(bannerList);
        goodsVo.setDescImgList(descImgList);
        goodsVo.setCategory(goodsCategoryService.queryByIdAndLanguageId(goodsVo.getCategoryId(), defaultLanguageId));
        return R.ok().put("goods", goodsVo);
    }

    @ApiOperation("根据商家用户ID获取商品")
    @GetMapping("/queryListByUserId/{userId}")
    @Login
    public R queryListByUserId(@PathVariable Long userId,@ApiIgnore @LoginUser UserInfo userInfo) {
        Long languageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            languageId = language.getId();
        }
        List<GoodsVo> goodsList = null;
        if(userId.intValue() == userInfo.getUserId().intValue()){
            goodsList = goodsService.queryListByUserIdAndLanguageId(userId,languageId);
        }else{
            goodsList = goodsService.queryListIssueByUserIdAndLanguageId(userId,languageId);
        }
        return R.ok().put("list",goodsList);
    }

    @ApiOperation("商品下单")
    @PostMapping("/buyGoods")
    @Login
    public R buyGoods(@RequestBody BuyGoodsForm form,@ApiIgnore @LoginUser UserInfo userInfo) {
        Goods goods = goodsService.getById(form.getGoodsId());
        if(goods.getStock() < 1 || goods.getStock() < form.getQuantity()){
            throw new RRException(ResponseStatusEnum.GOODS_STOCK_NOT_ENOUGH);
        }
        if(userInfo.getPassword() == null || "".equalsIgnoreCase(userInfo.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_NOT_EXIST);
        }
        if(!userInfo.getPassword().equalsIgnoreCase(form.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_ERROR);
        }
        BigDecimal amount = goods.getPrice().multiply(BigDecimal.valueOf(form.getQuantity()));
        UserAddress userAddress = userAddressService.getById(form.getUserAddressId());
        //创建订单
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setReceiverName(userAddress.getName());
        goodsOrder.setReceiverPhone(userAddress.getPhone());
        goodsOrder.setReceiverProvince(userAddress.getProvince());
        goodsOrder.setReceiverCity(userAddress.getCity());
        goodsOrder.setReceiverArea(userAddress.getArea());
        goodsOrder.setReceiverAddress(userAddress.getAddress());
        goodsOrder.setQuantity(form.getQuantity());
        goodsOrder.setStatus(0);
        goodsOrder.setOrderCode(StringUtils.createOrderNum());
        goodsOrder.setGoodsId(goods.getId());
        goodsOrder.setAmount(amount);
        goodsOrder.setUserId(userInfo.getUserId().longValue());
        goodsOrder.setPaymentId(form.getPaymentId());
        goodsOrderService.save(goodsOrder);
        if(userInfo.getBalance().compareTo(goodsOrder.getAmount()) == -1){
            return R.ok(ResponseStatusEnum.BALANCE_INSUFFICIENT).put("orderCode",goodsOrder.getOrderCode());
        }
        goodsOrder.setStatus(3);
        goodsOrderService.updateById(goodsOrder);
        //减库存
        goods.setStock(goods.getStock()-form.getQuantity());
        goodsService.updateById(goods);
        //扣除账户余额
        userInfo.setBalance(userInfo.getBalance().subtract(goodsOrder.getAmount()));
        userInfoService.update(userInfo);
        //增加账单记录
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setType(LogTypeEnum.OUTLAY.code);
        userBalanceLog.setBalance(userInfo.getBalance());
        userBalanceLog.setAmount(goodsOrder.getAmount().negate());
        userBalanceLog.setUserId(userInfo.getUserId().longValue());
        userBalanceLog.setStatus(UserBalanceLogStatusEnum.PAID_FOR_SHOP_ORDER.code);
        userBalanceLogService.save(userBalanceLog);
        UserInfo merchant = userInfoService.queryByUserId(goods.getUserId());
        if (merchant != null && merchant.getDel() != 1){
            List<Integer> userIds = new ArrayList<>();
            userIds.add(merchant.getUserId());
            List<String> users = new ArrayList<>();
            users.add(merchant.getEasemobId());
            try{
                messageService.push(userInfo.getNickName()+"购买了你的商品-"+goods.getTitle(),users,"2",goodsOrder.getOrderCode(),2);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return R.ok().put("orderCode",goodsOrder.getOrderCode());
    }

    @ApiOperation("一元购")
    @PostMapping("/oneBuy")
    @Login
    @Transactional
    public R oneBuy(@RequestBody BuyGoodsForm form,@ApiIgnore @LoginUser UserInfo userInfo) {
        Goods goods = goodsService.getById(form.getGoodsId());
        GoodsOneBuy goodsOneBuy = goodsOneBuyService.getOne(
                new QueryWrapper<GoodsOneBuy>()
                        .eq("goods_id",form.getGoodsId())
                        .eq("status",1)
                        .last("limit 1")
        );
        if(goodsOneBuy == null){
            if(goods.getIsOneBuy().intValue() == 1){
                goods.setIsOneBuy(0);
                goodsService.updateById(goods);
            }
            throw new RRException(ResponseStatusEnum.ONE_BUY_EVENT_OVER);
        }
        if(goodsOneBuy.getQuantity() < 1){
            if(goods.getIsOneBuy().intValue() == 1){
                goods.setIsOneBuy(0);
                goodsService.updateById(goods);
            }
            goodsOneBuyService.removeById(goodsOneBuy.getId());
            throw new RRException(ResponseStatusEnum.ONE_BUY_EVENT_OVER);
        }
        GoodsOrder tempOrder = goodsOrderService.getOne(
                new QueryWrapper<GoodsOrder>()
                .eq("user_id",userInfo.getUserId())
                .eq("goods_id",form.getGoodsId())
                .eq("amount",BigDecimal.ONE)
        );
        if(tempOrder != null){
            return R.ok(ResponseStatusEnum.ONLY_CAN_BUY_ONE);
        }
        if(goods.getStock() < 1){
            throw new RRException(ResponseStatusEnum.GOODS_STOCK_NOT_ENOUGH);
        }
        if(userInfo.getPassword() == null || "".equalsIgnoreCase(userInfo.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_NOT_EXIST);
        }
        if(!userInfo.getPassword().equalsIgnoreCase(form.getPassword())){
            return R.ok(ResponseStatusEnum.PAY_PASSWORD_ERROR);
        }
        if(userInfo.getFatherId() == null){
            throw new RRException(ResponseStatusEnum.USER_NOT_JOIN_TEAM_YET);
        }
        if(userInfo.getFatherId().intValue() != goods.getUserId().intValue()){
            return R.ok(ResponseStatusEnum.USER_DONT_BELONG_THE_TEAM);
        }
        if(form.getQuantity() > 1){
            return R.ok(ResponseStatusEnum.ONLY_CAN_BUY_ONE);
        }
        //修改一元购参数
        goodsOneBuy.setQuantity(goodsOneBuy.getQuantity()-1);
        goodsOneBuyService.updateById(goodsOneBuy);
        goods.setStock(goods.getStock()-1);
        goodsService.updateById(goods);
        BigDecimal amount = BigDecimal.ONE;
        UserAddress userAddress = userAddressService.getById(form.getUserAddressId());
        //创建订单
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setReceiverName(userAddress.getName());
        goodsOrder.setReceiverPhone(userAddress.getPhone());
        goodsOrder.setReceiverProvince(userAddress.getProvince());
        goodsOrder.setReceiverCity(userAddress.getCity());
        goodsOrder.setReceiverArea(userAddress.getArea());
        goodsOrder.setReceiverAddress(userAddress.getAddress());
        goodsOrder.setQuantity(form.getQuantity());
        goodsOrder.setStatus(0);
        goodsOrder.setOrderCode(StringUtils.createOrderNum());
        goodsOrder.setGoodsId(goods.getId());
        goodsOrder.setAmount(amount);
        goodsOrder.setUserId(userInfo.getUserId().longValue());
        goodsOrder.setPaymentId(form.getPaymentId());
        goodsOrderService.save(goodsOrder);
        if(userInfo.getBalance().compareTo(goodsOrder.getAmount()) == -1){
            return R.ok(ResponseStatusEnum.BALANCE_INSUFFICIENT).put("orderCode",goodsOrder.getOrderCode());
        }
        goodsOrder.setStatus(3);
        goodsOrderService.updateById(goodsOrder);
        //扣除账户余额
        userInfo.setBalance(userInfo.getBalance().subtract(goodsOrder.getAmount()));
        userInfoService.update(userInfo);
        //增加账单记录
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setType(LogTypeEnum.OUTLAY.code);
        userBalanceLog.setBalance(userInfo.getBalance());
        userBalanceLog.setAmount(goodsOrder.getAmount().negate());
        userBalanceLog.setUserId(userInfo.getUserId().longValue());
        userBalanceLog.setStatus(UserBalanceLogStatusEnum.PAID_FOR_SHOP_ORDER.code);
        userBalanceLogService.save(userBalanceLog);
        return R.ok().put("orderCode",goodsOrder.getOrderCode());
    }


    @Login
    @ApiOperation("商品发布")
    @PostMapping("/goodsRelease")
    public R goodsRelease(@RequestBody GoodsReleaseForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        if (null == form.getLogoUrl()) {
            throw new RRException(ResponseStatusEnum.NOT_LOGO_IMG);
        }
        if (null == form.getBannerList() || 0 == form.getBannerList().size()) {
            throw new RRException(ResponseStatusEnum.NOT_BANNER_IMG);
        }
        Goods goods = new Goods();
        goods.setCategoryId(form.getCategoryId());
        goods.setCountryId(form.getCountryId());
        goods.setDescription(form.getDescription());
        goods.setTitle(form.getTitle());
        goods.setPrice(form.getPrice());
        goods.setStock(form.getStock());
        goods.setPhone(form.getPhone());
        goods.setAddress(form.getAddress());
        if(userInfo.getIsMerchant() != 1){
            goods.setLinkUrl(form.getLinkUrl());
        }
        goods.setUserId(userInfo.getUserId().longValue());
        goods.setLogoUrl(form.getLogoUrl());
        goods.setStatus(1);
        goodsService.save(goods);
        GoodsLang goodsLang = new GoodsLang();
        goodsLang.setLanguageId(form.getLanguageId());
        goodsLang.setGoodsId(goods.getId());
        goodsLang.setLogoUrl(goods.getLogoUrl());
        goodsLang.setDescription(goods.getDescription());
        goodsLang.setTitle(goods.getTitle());
        goodsLangService.save(goodsLang);
        for (int i = 0; i < form.getBannerList().size(); i++) {
            GoodsImg goodsImg = form.getBannerList().get(i);
            goodsImg.setGoodsId(goods.getId());
            goodsImg.setLanguageId(form.getLanguageId());
            goodsImg.setOrderNum(i);
            goodsImg.setType(1);
            goodsImgService.save(goodsImg);
        }
        if(form.getDescImgList() != null){
            for (int i = 0; i < form.getDescImgList().size(); i++) {
                GoodsImg goodsImg = form.getDescImgList().get(i);
                goodsImg.setGoodsId(goods.getId());
                goodsImg.setLanguageId(form.getLanguageId());
                goodsImg.setOrderNum(i);
                goodsImg.setType(2);
                goodsImgService.save(goodsImg);
            }
        }
        return R.ok().put("goods",goods);
    }

    @Login
    @ApiOperation("商品修改")
    @PostMapping("/updateGoods")
    public R updateGoods(@RequestBody GoodsReleaseForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        Goods goods = goodsService.getById(form.getGoodsId());
        if(goods == null){
            throw new RRException(ResponseStatusEnum.PARAMS_IS_ERROR);
        }
        goods.setCategoryId(form.getCategoryId());
        goods.setCountryId(form.getCountryId());
        goods.setPrice(form.getPrice());
        goods.setStock(form.getStock());
        goods.setPhone(form.getPhone());
        goods.setAddress(form.getAddress());
        if(userInfo.getIsMerchant() != 1){
            goods.setLinkUrl(form.getLinkUrl());
        }else{
            goods.setLinkUrl(null);
        }
        goods.setUserId(userInfo.getUserId().longValue());
        if(form.getLogoUrl() != null && !"".equalsIgnoreCase(form.getLogoUrl())){
            goods.setLogoUrl(form.getLogoUrl());
        }
        goods.setStatus(form.getStatus());
        goodsService.updateById(goods);
        //删除原语言包
        goodsLangService.remove(
                new QueryWrapper<GoodsLang>()
                        .eq("goods_id",form.getGoodsId())
                        .eq("language_id",form.getLanguageId())
        );
        goodsImgService.remove(
                new QueryWrapper<GoodsImg>()
                        .eq("goods_id",goods.getId())
                        .eq("language_id",form.getLanguageId())
        );
        GoodsLang goodsLang = new GoodsLang();
        goodsLang.setLanguageId(form.getLanguageId());
        goodsLang.setGoodsId(goods.getId());
        goodsLang.setLogoUrl(goods.getLogoUrl());
        goodsLang.setDescription(goods.getDescription());
        goodsLang.setTitle(goods.getTitle());
        goodsLangService.save(goodsLang);
        if(form.getBannerList() != null){
            for (int i = 0; i < form.getBannerList().size(); i++) {
                GoodsImg goodsImg = form.getBannerList().get(i);
                goodsImg.setGoodsId(goods.getId());
                goodsImg.setLanguageId(form.getLanguageId());
                goodsImg.setOrderNum(i);
                goodsImg.setType(1);
                goodsImgService.save(goodsImg);
            }
        }
        if(form.getDescImgList() != null){
            for (int i = 0; i < form.getDescImgList().size(); i++) {
                GoodsImg goodsImg = form.getDescImgList().get(i);
                goodsImg.setGoodsId(goods.getId());
                goodsImg.setLanguageId(form.getLanguageId());
                goodsImg.setOrderNum(i);
                goodsImg.setType(2);
                goodsImgService.save(goodsImg);
            }

        }

        return R.ok();
    }

    @Login
    @ApiOperation("设置拼团活动")
    @PostMapping("/setGoodsGroup")
    public R setGoodsGroup(@RequestBody SetGoodsGroupForm form, @ApiIgnore @LoginUser UserInfo userInfo) {
        BigDecimal credits = form.getAward().multiply(BigDecimal.valueOf(form.getAwardNum()));
        if(userInfo.getCredits().compareTo(credits) == -1){
            throw new RRException(ResponseStatusEnum.CREDITS_INSUFFICIENT);
        }
        userInfo.setCredits(userInfo.getCredits().subtract(credits));
        userInfoService.update(userInfo);
        //增加积分记录
        UserCreditsLog userCreditsLog = new UserCreditsLog();
        userCreditsLog.setUserId(userInfo.getUserId().longValue());
        userCreditsLog.setAmount(credits.negate());
        userCreditsLog.setType(LogTypeEnum.OUTLAY.code);
        userCreditsLog.setStatus(UserCreditsLogStatusEnum.RELEASE_GOODS_GROUP_AD.code);
        userCreditsLog.setBalance(userInfo.getCredits());
        userCreditsLogService.save(userCreditsLog);
        GoodsGroup goodsGroup = goodsGroupService.getOne(new QueryWrapper<GoodsGroup>().eq("goods_id",form.getGoodsId()));
        Goods goods = goodsService.getById(form.getGoodsId());
        goods.setIsGroup(1);
        goodsService.updateById(goods);
        if(goodsGroup == null){
            goodsGroup = new GoodsGroup();
            goodsGroup.setGoodsId(form.getGoodsId());
            goodsGroup.setAwardNum(form.getAwardNum());
            goodsGroup.setAward(form.getAward());
            goodsGroup.setStatus(1);
            goodsGroup.setPrice(form.getPrice());
            goodsGroup.setPeriod(form.getPeriod());
            goodsGroup.setUserNum(form.getUserNum());
            goodsGroupService.save(goodsGroup);
        }else{
            goodsGroup.setGoodsId(form.getGoodsId());
            goodsGroup.setAwardNum(form.getAwardNum());
            goodsGroup.setAward(form.getAward());
            goodsGroup.setStatus(1);
            goodsGroup.setPrice(form.getPrice());
            goodsGroup.setPeriod(form.getPeriod());
            goodsGroup.setUserNum(form.getUserNum());
            goodsGroupService.updateById(goodsGroup);
        }
        return R.ok().put("goodsGroup",goodsGroup);
    }

    @Login
    @ApiOperation("停止拼团活动")
    @GetMapping("/stopGoodsGroup/{goodsId}")
    public R stopGoodsGroup(@PathVariable String goodsId, @ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsGroup goodsGroup = goodsGroupService.getOne(new QueryWrapper<GoodsGroup>().eq("goods_id",goodsId));
        Goods goods = goodsService.getById(goodsId);
        goods.setIsGroup(0);
        goodsService.updateById(goods);
        if(goodsGroup != null){
            goodsGroup.setStatus(0);
            goodsGroupService.updateById(goodsGroup);
        }
        if(goodsGroup.getAwardNum() > 0){
            BigDecimal credits = goodsGroup.getAward().multiply(BigDecimal.valueOf(goodsGroup.getAwardNum()));
            userInfo.setCredits(userInfo.getCredits().add(credits));
            userInfoService.update(userInfo);
            //TODO 这里需要增加积分变动记录
            //增加积分记录
            UserCreditsLog userCreditsLog = new UserCreditsLog();
            userCreditsLog.setUserId(userInfo.getUserId().longValue());
            userCreditsLog.setAmount(credits);
            userCreditsLog.setType(LogTypeEnum.INCOME.code);
            userCreditsLog.setStatus(UserCreditsLogStatusEnum.CANCELED_GOODS_GROUP_AD.code);
            userCreditsLog.setBalance(userInfo.getCredits());
            userCreditsLogService.save(userCreditsLog);
        }
        return R.ok();
    }

    @Login
    @ApiOperation("获取拼团活动信息")
    @GetMapping("/getGoodsGroup/{goodsId}")
    public R getGoodsGroup(@PathVariable Long goodsId) {
        Goods goods = goodsService.getById(goodsId);
        if(goods.getIsGroup() == 1){
            GoodsGroup goodsGroup = goodsGroupService.getOne(new QueryWrapper<GoodsGroup>().eq("goods_id",goodsId));
            return R.ok().put("goodsGroup",goodsGroup);
        }
        return R.ok();
    }

    @ApiOperation("图片上传")
    @PostMapping("/uploadImage")
    public R uploadImage(MultipartFile file) {
        String url = OssUtils.uploadToOss(file);
        return R.ok().put("url",url);
    }

    @ApiOperation("收藏商品列表")
    @GetMapping("/favoriteList")
    @Login
    public R favoriteList(@ApiIgnore @LoginUser UserInfo userInfo) {
        GoodsQuery query = new GoodsQuery();
        Long languageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            languageId = language.getId();
        }
        query.setLanguageId(languageId);
        query.setUserId(userInfo.getUserId().longValue());
        return R.ok().put("list", goodsService.queryFavoriteList(query));
    }

    @ApiOperation("添加商品收藏")
    @GetMapping("/addFavorite/{goodsId}")
    public R addFavorite(@PathVariable("goodsId") Long goodsId,@ApiIgnore @LoginUser UserInfo userInfo) {
        Goods goods = goodsService.getById(goodsId);
        if(goods == null){
            throw new RRException(ResponseStatusEnum.PARAMS_IS_ERROR);
        }
        UserFavoriteGoods userFavoriteGoods = userFavoriteGoodsService.getOne(
                new QueryWrapper<UserFavoriteGoods>()
                .eq("user_id",userInfo.getUserId())
                .eq("goods_id",goodsId)
        );
        if(userFavoriteGoods == null){
            userFavoriteGoods = new UserFavoriteGoods();
            userFavoriteGoods.setGoodsId(goodsId);
            userFavoriteGoods.setUserId(userInfo.getUserId().longValue());
            userFavoriteGoodsService.save(userFavoriteGoods);
        }
        return R.ok();
    }

    @ApiOperation("移除商品收藏")
    @DeleteMapping("/delFavorite/{goodsId}")
    public R delFavorite(@PathVariable("goodsId") Long goodsId,@ApiIgnore @LoginUser UserInfo userInfo) {
        userFavoriteGoodsService.remove(new QueryWrapper<UserFavoriteGoods>()
                .eq("user_id",userInfo.getUserId())
                .eq("goods_id",goodsId)
        );
        return R.ok();
    }

    @ApiOperation("获取一元购详情")
    @GetMapping("/getOneBuyInfo/{goodsId}")
    public R getOneBuyInfo(@PathVariable("goodsId") Long goodsId) {
        GoodsOneBuy goodsOneBuy = goodsOneBuyService.getOne(
                new QueryWrapper<GoodsOneBuy>()
                .eq("goods_id",goodsId)
        );
        return R.ok().put("goodsOneBuy",goodsOneBuy);
    }

    @ApiOperation("设置商品一元购")
    @PostMapping("/setOneBuy")
    public R setOneBuy(@RequestBody GoodsOneBuyForm form) {
        Goods goods = goodsService.getById(form.getGoodsId());
        if(goods.getIsOneBuy().intValue() == 1){
            goods.setIsOneBuy(0);
            goodsService.updateById(goods);
        }
        goodsOneBuyService.remove(new QueryWrapper<GoodsOneBuy>()
                .eq("goods_id",form.getGoodsId())
        );
        GoodsOneBuy goodsOneBuy = new GoodsOneBuy();
        goodsOneBuy.setGoodsId(form.getGoodsId());
        goodsOneBuy.setQuantity(form.getQuantity());
        goodsOneBuy.setTotalQuantity(form.getQuantity());
        goodsOneBuy.setExpireTime(LocalDateTime.parse(form.getExpireTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        goodsOneBuy.setStatus(0);
        goodsOneBuyService.save(goodsOneBuy);
        return R.ok();
    }

    @ApiOperation("取消商品一元购")
    @GetMapping("/cancelOneBuy/{goodsId}")
    public R cancelOneBuy(@PathVariable("goodsId") Long goodsId) {
        Goods goods = goodsService.getById(goodsId);
        goods.setIsOneBuy(0);
        goodsService.updateById(goods);
        goodsOneBuyService.remove(new QueryWrapper<GoodsOneBuy>()
                .eq("goods_id",goodsId)
        );
        return R.ok();
    }

    @ApiOperation("获取免费抢详情")
    @GetMapping("/getRushInfo/{goodsId}")
    public R getRushInfo(@PathVariable("goodsId") Long goodsId) {
        GoodsRush goodsRush = goodsRushService.getOne(
                new QueryWrapper<GoodsRush>()
                        .eq("goods_id",goodsId)
        );
        return R.ok().put("goodsRush",goodsRush);
    }

    @ApiOperation("设置商品免费抢")
    @PostMapping("/setRush")
    public R setRush(@RequestBody GoodsRushForm form) {
        Goods goods = goodsService.getById(form.getGoodsId());
        if(goods.getIsRush().intValue() == 1){
            goods.setIsRush(0);
            goodsService.updateById(goods);
        }
        goodsRushService.remove(new QueryWrapper<GoodsRush>()
                .eq("goods_id",form.getGoodsId())
        );
        GoodsRush goodsRush = new GoodsRush();
        goodsRush.setGoodsId(form.getGoodsId());
        goodsRush.setQuantity(form.getQuantity());
        goodsRush.setTotalQuantity(form.getQuantity());
        goodsRush.setExpireTime(LocalDateTime.parse(form.getExpireTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        goodsRush.setStatus(0);
        goodsRushService.save(goodsRush);
        return R.ok();
    }

    @ApiOperation("取消商品免费抢")
    @GetMapping("/cancelRush/{goodsId}")
    public R cancelRush(@PathVariable("goodsId") Long goodsId) {
        Goods goods = goodsService.getById(goodsId);
        goods.setIsRush(0);
        goodsService.updateById(goods);
        goodsRushService.remove(new QueryWrapper<GoodsRush>()
                .eq("goods_id",goodsId)
        );
        return R.ok();
    }

    @ApiOperation("根据商品ID获取免费抢奖品列表")
    @GetMapping("/getPrizes/{goodsId}")
    public R getPrizes(@PathVariable("goodsId") Long goodsId) {
        List<PrizeVo> list = new ArrayList<>();
        list.add(new PrizeVo(1,1));
        list.add(new PrizeVo(2,3));
        list.add(new PrizeVo(3,2,BigDecimal.valueOf(5)));
        list.add(new PrizeVo(4,3));
        list.add(new PrizeVo(5,3));
        return R.ok().put("list",list);
    }

    @ApiOperation("根据商品ID抽奖")
    @GetMapping("/lottery/{goodsId}")
    public R lottery(@PathVariable("goodsId") Long goodsId,@ApiIgnore @LoginUser UserInfo userInfo) {
        Goods goods = goodsService.getById(goodsId);
        GoodsRush goodsRush = goodsRushService.getOne(new QueryWrapper<GoodsRush>()
                .eq("goods_id",goodsId)
                .eq("status",1)
                .last("limit 1")
        );
        if(goods.getIsRush().intValue() == 0 || goodsRush == null || goodsRush.getQuantity().intValue() < 1){
            return R.error(ResponseStatusEnum.RUSH_EVENT_OVER);
        }
        if(userInfo.getLotteryTimes() == null || userInfo.getLotteryTimes() < 1){
            return R.error(ResponseStatusEnum.LOTTERY_TIMES_RAN_OUT);
        }
        Random r = new Random();
        int lotteryNumber = r.nextInt(5) + 1;
        if(lotteryNumber == 1){
            goodsRush.setQuantity(goodsRush.getQuantity()-1);
            goodsRushService.updateById(goodsRush);
            UserAddress userAddress = userAddressService.getOne(
                    new QueryWrapper<UserAddress>()
                    .eq("user_id",userInfo.getUserId())
                    .eq("is_default",1).last("limit 1")
            );
            GoodsOrder goodsOrder = new GoodsOrder();
            if(userAddress != null){
                goodsOrder.setReceiverName(userAddress.getName());
                goodsOrder.setReceiverPhone(userAddress.getPhone());
                goodsOrder.setReceiverProvince(userAddress.getProvince());
                goodsOrder.setReceiverCity(userAddress.getCity());
                goodsOrder.setReceiverArea(userAddress.getArea());
                goodsOrder.setReceiverAddress(userAddress.getAddress());
            }
            goodsOrder.setQuantity(1);
            goodsOrder.setStatus(3);
            goodsOrder.setOrderCode(StringUtils.createOrderNum());
            goodsOrder.setGoodsId(goodsId);
            goodsOrder.setAmount(BigDecimal.ZERO);
            goodsOrder.setUserId(userInfo.getUserId().longValue());
            goodsOrder.setPaymentId(1L);
            goodsOrderService.save(goodsOrder);
        }
        if(lotteryNumber == 3){
            BigDecimal credits = new BigDecimal(sysConfigService.getValue("LOTTERY_CREDITS"));
            userInfo.setCredits(userInfo.getCredits().add(credits));
            //增加积分记录
            UserCreditsLog userCreditsLog = new UserCreditsLog();
            userCreditsLog.setUserId(userInfo.getUserId().longValue());
            userCreditsLog.setAmount(credits);
            userCreditsLog.setType(LogTypeEnum.INCOME.code);
            userCreditsLog.setStatus(UserCreditsLogStatusEnum.LOTTERY.code);
            userCreditsLog.setBalance(userInfo.getCredits());
            userCreditsLogService.save(userCreditsLog);
        }
        userInfo.setLotteryTimes(userInfo.getLotteryTimes()-1);
        userInfoService.update(userInfo);
        return R.ok().put("lotteryNumber",lotteryNumber);
    }

}
