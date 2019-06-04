package com.bzs.utils.enumUtil;

/**
 * @program: insurance_bzs
 * @description: 根据车牌获取cityCode值
 * @author: dengl
 * @create: 2019-06-04 09:02
 */
public enum CityCodeEnum {

    /*
     * 北京汽车牌照
     */
    北京(1,"京"),
    京A(1,"京A"),
    京B(1,"京B"),
    京C(1,"京C"),
    京D(1,"京D"),
    京E(1,"京E"),
    京F(1,"京F"),
    京G(1,"京G"),
    京H(1,"京H"),
    京J(1,"京J"),
    京K(1,"京K"),
    京L(1,"京L"),
    京M(1,"京M"),
    京Y(1,"京Y"),
    /*
     * 重庆汽车牌照
     */
    重庆(2,"渝"),
    重庆市区江南(2,"渝A"),
    重庆市区江北(2,"渝B"),
    永川区(2,"渝C"),
    万州区(2,"渝F"),
    涪陵区(2,"渝G"),
    黔江区(2,"渝H"),
    /*
     * 天津汽车牌照
     */
    天津(3,"津"),
    津A(3,"津A"),
    津B(3,"津B"),
    津C(3,"津C"),
    津D(3,"津D"),
    津E(3,"津E"),
    津F(3,"津F"),
    津G(3,"津G"),
    津H(3,"津H"),
    /*
     * 成都汽车牌照
     */
    成都(4,"川A"),
    /*
     * 昆明汽车牌照
     */
    昆明(5,"云A"),
    /*
     * 上海汽车牌照,沪R----->崇明、长兴、横沙
     */
    上海(6,"沪"),
    沪A(6,"沪A"),
    沪B(6,"沪B"),
    沪C(6,"沪C"),
    沪D(6,"沪D"),
    沪R(6,"沪R"),
    /*
     * 宁夏回族自治区
     * 银川汽车牌照
     */
    银川(7,"宁A"),


    /*
     * 江苏
     * 南京汽车牌照
     */
    南京(8,"苏A"),
    /*
     * 浙江
     * 杭州汽车牌照
     *
     */
    杭州(9,"浙A"),
    /*
     * 福建
     * 福州汽车牌照
     *
     */
    福州(10,"闽A"),
    /*
     * 广州
     * 深圳汽车牌照
     *
     */
    深圳(11,"粤B"),
    /*
     *
     * 河北
     * 石家庄
     */
    石家庄(12,"翼A"),
    /*
     * 安徽
     * 芜湖
     */
    芜湖(13,"皖B"),
    /*
     * 广东
     * 广州
     */
    广州(14,"粤A"),
    /*
     * 福建
     * 厦门
     */
    厦门(15,"闽D"),
    /*
     * 江苏
     * 苏州
     */
    苏州(16,"苏E"),
    /*
     * 广州
     * 东莞
     */
    东莞(17,"粤S"),
    /*
     * 山东
     * 济南
     */
    济南(18,"鲁A"),
    /*
     * 湖北
     * 武汉
     */
    武汉(19,"鄂A"),
    /*
     * 广东
     * 佛山
     */
    佛山(20,"粤E"),
    /*
     * 江苏
     * 无锡
     */
    无锡(21,"苏B"),
    /*
     * 山东
     * 烟台
     */
    烟台(22,"鲁F"),
    /*
     * 江苏
     * 泰州
     */
    泰州(23,"苏M"),
    /*
     * 吉林
     * 长春
     *
     */
    长春(25,"吉A"),

    /*
     * 河南
     * 郑州
     *
     */
    郑州(27,"豫A"),

    /*
     * 山东
     * 青岛
     */
    青岛(28,"鲁B"),

    /*
     * 新疆
     */
    新疆(29,"新"),
    乌鲁木齐(29,"新A"),
    昌吉回族自治州(29,"新B"),
    石河子市(29,"新C"),
    奎屯市(29,"新D"),
    博尔塔拉蒙古自治州(29,"新E"),
    伊犁哈萨克自治州直辖县(29,"新F"),
    塔城市(29,"新G"),
    阿勒泰市(29,"新H"),
    克拉玛依市(29,"新J"),
    吐鲁番市(29,"新K"),
    哈密市(29,"新L"),
    巴音郭楞蒙古自治州(29,"新M"),
    阿克苏市(29,"新N"),
    克孜勒苏柯尔克孜自治州(29,"新P"),
    喀什市(29,"新Q"),
    和田市(29,"新R"),

    /*
     * 山东
     * 聊城
     */
    聊城(32,"鲁P"),

    /*
     * 江苏
     * 盐城
     *
     */
    盐城(33,"苏J"),
    /*
     * 江苏
     * 南通、常州
     */
    南通(34,"苏F"),
    常州(35,"苏D"),

    /*
     * 河北
     * 保定
     *
     */
    保定(36,"冀F"),

    /*
     * 辽宁
     * 沈阳
     *
     */
    沈阳(37,"辽A"),

    /*
     * 浙江
     * 台州
     */
    台州(38,"浙J"),
    /*
     * 辽宁
     * 盘锦
     */
    盘锦(39,"辽L"),
    /*
     * 浙江
     * 嘉兴
     */
    嘉兴(40,"浙F");
    private int index;
    private String cityName;
    CityCodeEnum(int index,String cityName){
        this.index=index;
        this.cityName=cityName;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public static CityCodeEnum getByValue(int value) {
        for(CityCodeEnum typeEnum : CityCodeEnum.values()) {
            if(typeEnum. index== value) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("No element matches " + value);
    }
    public static int getByCityName(String CA) {
        for(CityCodeEnum typeEnum : CityCodeEnum.values()) {
            if(typeEnum.cityName.equals(CA)) {
                return typeEnum.index;
            }
        }
        throw new IllegalArgumentException("No element matches " + CA);
    }


}
