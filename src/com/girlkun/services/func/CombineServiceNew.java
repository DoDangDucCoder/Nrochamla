package com.girlkun.services.func;
import com.girlkun.consts.ConstNpc;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerNotify;
import com.girlkun.network.io.Message;
import com.girlkun.services.*;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.*;
import java.util.stream.Collectors;

public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;

    private static final int COST = 500000000;

    private static final int TIME_COMBINE = 1500;

    private static final byte MAX_STAR_ITEM = 9;
    private static final byte MAX_LEVEL_ITEM = 8;
    private static final byte MAX_STAR_TINH_AN = 1;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int PHA_LE_HOA_CAI_TRANG = 504;
    public static final int CHUYEN_HOA_TRANG_BI = 502;
    public static final int PHA_LE_HOA_TINH_AN = 503;
//    public static final int DOI_VE_HUY_DIET = 503;
//    public static final int DAP_SET_KICH_HOAT = 504;
//    public static final int DOI_MANH_KICH_HOAT = 505;
//    public static final int DOI_CHUOI_KIEM = 506;
//    public static final int DOI_LUOI_KIEM = 507;
//    public static final int DOI_KIEM_THAN = 508;

    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int MO_CHI_SO_BONG_TAI = 517;
    public static final int LAM_PHEP_NHAP_DA = 512;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int PHAN_RA_DO_THAN_LINH = 514;
    public static final int NANG_CAP_DO_TS = 515;
    public static final int NANG_CAP_SKH_VIP = 516;
    public static final int NANG_CAP_BONG_TAI2 = 518;
    public static final int MO_CHI_SO_BONG_TAI2 = 519;
    public static final int NANG_CAP_BONG_TAI3 = 521;
    public static final int NANG_TL_LEN_HUY_DIET = 2512;
    public static final int NANG_HUY_DIET_LEN_SKH = 2513;
    public static final int NANG_HUY_DIET_LEN_SKH_VIP = 2514;
    public static final int NANG_CAP_TS = 2515;
    public static final int MO_CHI_SO_BONG_TAI3 = 556;
    public static final int NANG_CAP_LINH_THU = 520;
    public static final int NANG_CAP_PET = 600;
    public static final int NANG_CAP_PT_skH = 599;
    
    private static final int GOLD_BONG_TAI = 500_000_000;
    private static final int GEM_BONG_TAI = 5_000;
    private static final int RATIO_BONG_TAI = 50;
    private static final int RATIO_NANG_CAP = 45;
    

    private final Npc baHatMit;
    private final Npc thiensu;

    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.thiensu = NpcManager.getNpc(ConstNpc.THIENSU);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {

            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 454) {
                            bongTai = item;
                        } else if (item.template.id == 933) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                            Item thoivang = null;
                            {
                                thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                            }
                            if (thoivang == null || thoivang.quantity < 5) {
                                Service.getInstance().sendThongBao(player, "Sắp hết thỏi vàng rồi !");

                            } else if (player.inventory.gold < 500_000_000) {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 1);
                                player.inventory.gold += 500_000_000;
                            }
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; //sao pha lê đã ép
                    int starEmpty = 0; //lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (Item.ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.rubyCombine = getRubyPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\n" + player.combineNew.rubyCombine +  " ngọc\n" + "1 lần", "Nâng cấp\n" + (player.combineNew.rubyCombine * 500) + " ngọc\n" + "10 Lần", "Nâng cấp\n" + (player.combineNew.rubyCombine * 5000) + " ngọc\n" + "100 lần");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;
            case PHA_LE_HOA_CAI_TRANG:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isCaitrang(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa1(star);
                            player.combineNew.rubyCombine = getRubyPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa1(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\ncần " + player.combineNew.rubyCombine + " ngọc");
                                Item thoivang = null;
                                {
                                    thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                }
                                if (thoivang == null || thoivang.quantity < 5) {
                                    Service.getInstance().sendThongBao(player, "Sắp hết thỏi vàng rồi !");

                                } else if (player.inventory.gold < 500_000_000) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 1);
                                    player.inventory.gold += 500_000_000;
                                }
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;    
            case PHA_LE_HOA_TINH_AN:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 34) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_TINH_AN) {
                            player.combineNew.goldCombine = getGoldTinhAn(star);
                            player.combineNew.rubyCombine = getGemRuby(star);
                            player.combineNew.ratioCombine = getRatioTinhAn(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 34) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\ncần " + player.combineNew.rubyCombine + " ngọc");
                                Item thoivang = null;
                                {
                                    thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                }
                                if (thoivang == null || thoivang.quantity < 5) {
                                    Service.getInstance().sendThongBao(player, "Sắp hết thỏi vàng rồi !");

                                } else if (player.inventory.gold < 1_000_000_000) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 2);
                                    player.inventory.gold += 1_000_000_000;
                                }
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã được nâng cấp", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể nâng cấp", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 vật phẩm để nâng cấp", "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6 || level == 7) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;

            case PHAN_RA_DO_THAN_LINH:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con hãy đưa ta đồ thần linh để phân rã", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 555 && item.template.id <= 567) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
                        }
                    }
                    if (couponAdd == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã đồ thần linh thôi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi phân rải vật phẩm\n|7|"
                            + "Bạn sẽ nhận được : " + couponAdd + " Điểm\n"
                            + (500000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(500000000) + " vàng";

                    if (player.inventory.gold < 500000000) {
                        this.baHatMit.npcChat(player, "Hết tiền rồi\nNghèo ít thôi con");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_DO_THAN_LINH,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(500000000) + " vàng", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã 1 lần 1 món đồ thần linh", "Đóng");
                }
                break;
            case NANG_CAP_DO_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 món Hủy Diệt bất kì và 1 món Thần Linh cùng loại", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh thiên sứ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " thiên sứ tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.thiensu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.thiensu.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_TS,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.thiensu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.thiensu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_SKH_VIP:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món thiên sứ và 2 món SKH ngẫu nhiên", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thiên sứ", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ kích hoạt ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
                
            case NANG_CAP_BONG_TAI2:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 933) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case NANG_TL_LEN_HUY_DIET:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món thần linh, ta sẽ cho 1 món huỷ diệt tương ứng", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh rồi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get().typeName() + " Huỷ diệt tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(500000000) + " vàng";

                    if (player.inventory.gold < 500000000) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(500000000) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_TS:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item dohd = null;
                    Item manhts = null;
                    Item dts = null;
                    
                    for (Item item : player.combineNew.itemsCombine) {
                        if (dohd != null && dohd.isDHD()) {
                            dohd = item;
                        } else if (manhts != null && manhts.isManhTS()) {
                            manhts = item;
                        } else if (dts != null && dts.isDTS()) {
                            dts = item;    
                        }
                    }
                    if (dohd != null && manhts != null && manhts.quantity >= 999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Đồ Thiên Sứ" + "\n|2|";
                        int tiLe =  50;
                        if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        ItemService.gI().OpenDoTs(player, player.gender, dts.template.type);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dts, 1);
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Đồ Hủy Diệt và X999 Mảnh Thiên Sứ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Đồ Hủy Diệt và X999 Mảnh Thiên Sứ", "Đóng");
                }
                break;
            case NANG_HUY_DIET_LEN_SKH:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món huỷ diệt, ta sẽ cho 1 món huỷ diệt tương ứng", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ huỷ diệt rồi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get().typeName() + " kích hoạt tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(500000000) + " vàng";

                    if (player.inventory.gold < 500000000) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(500000000) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_HUY_DIET_LEN_SKH_VIP:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 3 món huỷ diệt....", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ huỷ điệt rồi", "Đóng");
                        return;
                    }
                    Item item1 = player.combineNew.itemsCombine.get(0);
                    Item item2 = player.combineNew.itemsCombine.get(1);
                    Item item3 = player.combineNew.itemsCombine.get(2);

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(500000000) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI3:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1109) {
                            bongTai = item;
                        } else if (item.template.id == 933) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: 30%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 3 và 9999 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 3 và 9999 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case NANG_CAP_LINH_THU:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item linhthu = null;
                    Item thangtinhthach = null;
                    Item thucan = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id >= 2019 && item.template.id <= 2026) {
                            linhthu = item;
                        } else if (item.template.id == 2030) {
                            thangtinhthach = item;
                        } else if (item.template.id >= 663 && item.template.id <= 667) {
                            thucan = item;
                        }
                    }
                    if (linhthu != null && thangtinhthach != null && thucan != null && thangtinhthach.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Linh Thú Siêu Cấp" + "\n|2|";
                        for (Item.ItemOption io : linhthu.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Linh Thú, X99 Đá Ma Thuât và 1 Thức Ăn", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Linh Thú, X99 Đá Ma Thuât và 1 Thức Ăn", "Đóng");
                }
                break;
            case NANG_CAP_PET:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item pet = null;
                    Item thangtinhthach = null;
                    Item thucan = null;
                    for (Item item : player.combineNew.itemsCombine) {
                         if (item.template.id >= 1145 && item.template.id <= 2026) {
                            pet = item;
                        } else if (item.template.id == 2030) {
                            thangtinhthach = item;
                        } else if (item.template.id >= 663 && item.template.id <= 667) {
                            thucan = item;
                        }
                    }
                    if (pet != null && thangtinhthach != null && thucan != null && thucan.quantity >= 50 && thangtinhthach.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "PET Siêu Cấp" + "\n|2|";
                        for (Item.ItemOption io : pet.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 PET, X99 Đá Ma Thuât và 1 Thức Ăn", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 PET, X99 Đá Ma Thuât và 1 Thức Ăn", "Đóng");
                }
                break;    

            case NANG_CAP_PT_skH:
                if (player.combineNew.itemsCombine.size() != 2) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 ITEM kish hoat va 1 item than linh", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item itemkh = null;
                    Item itemthanlinh = null;

                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isDTL()) {
                            itemthanlinh = item;
                        }
                        if (item.isSKHNangCap()) {
                            itemkh = item;
                        }

                    }
                    if (itemkh == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 ITEM kish hoat co the nang cap", "Đóng");
                        return;
                    }

                    if (itemthanlinh == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 ITEM kish hoat", "Đóng");
                        return;
                    }
                    if (itemkh.template.type != itemthanlinh.template.type) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 ITEM kish hoat va item than linh cung loai", "Đóng");
                        return;
                    }
                    if (itemkh != null && itemthanlinh != null) {

                        player.combineNew.goldCombine = 100000000;

                        String npcSay = "" + itemkh.template.name + "\n|2|";
                        for (Item.ItemOption io : itemkh.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "Tang 1% chi so cho set kich hoat\n";
                        npcSay += "|7|Tỉ lệ thành công: " + (50 - getParamOption(itemkh, 212)) + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần 100tr vang");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 ITEM kish hoat va item than linh cung loai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 ITEM kish hoat va item than linh cung loai", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI2:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 2064) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;

            case MO_CHI_SO_BONG_TAI3:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1109) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 4, 99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 4, 99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
        switch (player.combineNew.typeCombine) {
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case PHA_LE_HOA_CAI_TRANG:
                phaLeHoaCaiTrang(player);
                break;    
            case CHUYEN_HOA_TRANG_BI:
                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
            case PHAN_RA_DO_THAN_LINH:
                phanradothanlinh(player);
                break;
            case NANG_CAP_DO_TS:
                openDTS(player);
                break;
            case NANG_CAP_SKH_VIP:
                openSKHVIP(player);
                break;
            case NANG_TL_LEN_HUY_DIET:
                nangCapThanLinhLenHuyDiet(player);
            case NANG_CAP_TS:
                nangcapts(player);    
                break;
            case NANG_HUY_DIET_LEN_SKH:
                huyDietLenKichHoatThuong(player);
                break;
            case NANG_HUY_DIET_LEN_SKH_VIP:
                huyDietLenKichHoatVIP(player);
                break;
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
                break;
            case NANG_CAP_BONG_TAI2:
                nangCapBongTai2(player);
                break;
            case NANG_CAP_BONG_TAI3:
                nangCapBongTai4(player);
                break;
            case MO_CHI_SO_BONG_TAI2:
                moChiSoBongTai2(player);

                break;
            case MO_CHI_SO_BONG_TAI3:
                moChiSoBongTai4(player);

                break;
            case NANG_CAP_LINH_THU:
                moChiSolinhthu(player);
                break;
            case NANG_CAP_PET:
                moChiSoPET(player);
                break;    
            case NANG_CAP_PT_skH:
                dapChisoDoKichHoat(player);
                break;
            case PHA_LE_HOA_TINH_AN:
                buff(player);
                break;

        }
        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }

    public void GetTrangBiKichHoathuydiet(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1500, 2000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(100, 150)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(90, 150)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15, 20)));
        }

        if (randomSkh <= 5) { // tile ra option vip
            item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
            item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
            item.itemOptions.add(new ItemOption(30, 0));
        } else {// 
            item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
            item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
            item.itemOptions.add(new ItemOption(30, 0));
        }

        item.itemOptions.add(new ItemOption(73, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void GetTrangBiHuyDiet(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1500, 2000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(100, 150)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(90, 150)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15, 20)));
        }
//        if (randomSkh <= 20) {//tile ra do kich hoat
//            if (randomSkh <= 5) { // tile ra option vip
//                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
//                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
//                item.itemOptions.add(new ItemOption(30, 0));
//            } else {// 
//                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
//                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
//                item.itemOptions.add(new ItemOption(30, 0));
//            }
//        }
        item.itemOptions.add(new ItemOption(73, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void GetTrangBiKichThanLinh(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
//        int[][] paramNormal = {{139,140},{142,144},{136,138}};
//        int[][] optionVIP = {{129},{131},{134}};
//        int[][] paramVIP = {{141},{143},{137}};
//        int random = Util.nextInt(optionNormal.length);
//        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1500, 2000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(100, 150)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(90, 150)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15, 20)));

        } else {// 
            item.itemOptions.add(new ItemOption(30, 0));
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void GetTrangBiKichHoatthiensu(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000, 20000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private void doiKiemThan(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item keo = null, luoiKiem = null, chuoiKiem = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 2015) {
                    keo = it;
                } else if (it.template.id == 2016) {
                    chuoiKiem = it;
                } else if (it.template.id == 2017) {
                    luoiKiem = it;
                }
            }
            if (keo != null && keo.quantity >= 99 && luoiKiem != null && chuoiKiem != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2018);
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 15)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 15)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 15)));
                    if (Util.isTrue(80, 100)) {
                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                    }
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 99);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, luoiKiem, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, chuoiKiem, 1);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiChuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhNhua = player.combineNew.itemsCombine.get(0);
            if (manhNhua.template.id == 2014 && manhNhua.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2016);
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhNhua, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiLuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhSat = player.combineNew.itemsCombine.get(0);
            if (manhSat.template.id == 2013 && manhSat.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2017);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhSat, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiManhKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }

            if (nr1s != null && doThan != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
                    int tiLe = buaBaoVe != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) 2009);
                        item.itemOptions.add(new Item.ItemOption(30, 0));
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, doThan, 1);
                    if (buaBaoVe != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, buaBaoVe, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            }
        }
    }

    private void phanradothanlinh(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            player.inventory.gold -= 500000000;
            List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
            Item item = player.combineNew.itemsCombine.get(0);
            int couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
            sendEffectSuccessCombine(player);
            player.inventory.coupon += couponAdd;
            this.baHatMit.npcChat(player, "Con đã nhận được " + couponAdd + " điểm");
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            player.combineNew.itemsCombine.clear();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        // new update 2 mon huy diet + 1 mon than linh(skh theo style) +  5 manh bat ki
        if (player.combineNew.itemsCombine.size() != 4) {
            Service.getInstance().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (player.inventory.gold < COST) {
            Service.getInstance().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).findFirst().get();
        List<Item> itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 1).findFirst().get();
        int tiLe = itemTL != null ? 30 : 100;
        if (Util.isTrue(tiLe, 100)) {
            player.inventory.gold -= COST;
            sendEffectSuccessCombine(player);
            short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

            Item itemTS = ItemService.gI().DoThienSu(itemIds[itemTL.template.gender > 2 ? player.gender : itemTL.template.gender][itemManh.typeIdManh()], itemTL.template.gender);
            InventoryServiceNew.gI().addItemBag(player, itemTS);

            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemManh, 99);
            itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            player.inventory.gold -= COST;
            sendEffectFailCombine(player);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemManh, 1);
            itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 454) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 9999);
                    bongTai.template = ItemService.gI().getTemplate(921);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 2));
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 999);
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item thangtinhthach = null;
            Item thucan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    linhthu = item;
                } else if (item.template.id == 934) {
                    thangtinhthach = item;
                } else if (item.template.id == 935) {
                    thucan = item;
                }
            }
            if (linhthu != null && thangtinhthach != null && thangtinhthach.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thangtinhthach, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thucan, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.clear();
                    linhthu.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 25)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 25)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 25)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(10, 25)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(10, 25)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(10, 25)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(10, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTai2(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 9999);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(2064);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 2));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTai4(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 2064) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 19999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 19999);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 1000);
                    bongTai.template = ItemService.gI().getTemplate(1109);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 2));
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 5000);
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai2(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item thangtinhthach = null;
            Item thucan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 2064) {
                    linhthu = item;
                } else if (item.template.id == 934) {
                    thangtinhthach = item;
                } else if (item.template.id == 935) {
                    thucan = item;
                }
            }
            if (linhthu != null && thangtinhthach != null && thangtinhthach.quantity >= 999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thangtinhthach, 999);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thucan, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.clear();
                    linhthu.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 25)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 25)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai4(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item thangtinhthach = null;
            Item thucan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1109) {
                    linhthu = item;
                } else if (item.template.id == 934) {
                    thangtinhthach = item;
                } else if (item.template.id == 935) {
                    thucan = item;
                }
            }
            if (linhthu != null && thangtinhthach != null && thangtinhthach.quantity >= 9999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;

                InventoryServiceNew.gI().subQuantityItemsBag(player, thucan, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.clear();
                    linhthu.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 35)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 35)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 35)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 35)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 35)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 35)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 35)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 35)));
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, thangtinhthach, 9999);
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, thangtinhthach, 999);
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    public void openSKHVIP(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đồ thiên sứ");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu đồ kích hoạt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (itemTS.template.gender == 3 || itemTS.template.type == 4) {
                itemId = Manager.radaSKHVip[Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.radaSKHVip[6];
                }
            } else {
                itemId = Manager.doSKHVip[itemTS.template.gender][itemTS.template.type][Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.doSKHVip[itemTS.template.gender][itemTS.template.type][6];
                }
            }
            int skhId = ItemService.gI().randomSKHId(itemTS.template.gender);
            Item item;
            if (new Item(itemId).isDTL()) {
                item = Util.ratiItemTL(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTS, 1);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void dapDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dhd = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 650 && item.template.id <= 662) {
                        dhd = item;
                    } else if (item.template.id >= 555 && item.template.id <= 567) {
                        dtl = item;
                    }
                }
            }
            if (dhd != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tiLe = dtl != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0(dhd.template.gender, dhd.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dhd, 1);
                    if (dtl != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private int getParamOption(Item item, int id) {
        for (ItemOption ito : item.itemOptions) {
            if (ito.optionTemplate.id == id) {
                return ito.param;
            }
        }
        return 0;
    }

    private int getIndex(Item item, int id) {
        for (int i = 0; i < item.itemOptions.size(); i++) {
            if (item.itemOptions.get(i).optionTemplate.id == id) {
                return i;
            }
        }
        return -1;
    }

    private void dapChisoDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dkh = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isSKHNangCap()) {
                        dkh = item;
                    } else if (item.isDTL()) {
                        dtl = item;
                    }
                }
            }
            if (dkh.template.type != dtl.template.type) {
                return;
            }
            if (dkh != null && dtl != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= 100000000) {
                    player.inventory.gold -= 100000000;
                    int param = getParamOption(dkh, 212);
                    if (Util.isTrue(50 - param, 100)) {
                        sendEffectSuccessCombine(player);

                        System.out.println("Param" + param);
                        if (param == 0) {
                            dkh.itemOptions.add(dkh.itemOptions.size() - 1, new ItemOption(212, 1));
                        } else {
                            int idx = getIndex(dkh, 212);
                            System.out.println("Indez" + idx);
                            if (idx != -1) {
                                dkh.itemOptions.remove(idx);
                                dkh.itemOptions.add(dkh.itemOptions.size() - 1, new ItemOption(212, param = param + 1));
                            }
                        }
                        System.out.println(dkh.getInfo());
//                        InventoryServiceNew.gI().addItemBag(player, itemskh);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    public void huyDietLenKichHoatVIP(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item item1 = player.combineNew.itemsCombine.get(0);
            Item item2 = player.combineNew.itemsCombine.get(1);
            Item item3 = player.combineNew.itemsCombine.get(2);
            if ((item1.isDHD() && item2.isDHD() && item3.isDHD())) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.inventory.gold < 200000000) {
                        Service.getInstance().sendThongBao(player, "Con cần thêm vàng để đổi...");
                        return;
                    }
                    player.inventory.gold -= 200000000;
                    if (Util.isTrue(30, 100)) {
                        int type = item1.template.type;
                        int[][][] items = {{{0, 3, 33, 34, 136, 137, 138, 139, 230, 231, 232, 233},// áo
                        {6, 9, 35, 36, 140, 141, 142, 143, 242, 243, 244, 245}, // quần
                        {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257}, // găng
                        {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269}, // giày
                        {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}}, // rada

                        {{1, 4, 41, 42, 152, 153, 154, 155, 234, 235, 236, 237},
                        {7, 10, 43, 44, 156, 157, 158, 159, 246, 247, 248, 249},
                        {22, 25, 45, 46, 160, 161, 162, 163, 258, 259, 260, 261},
                        {28, 31, 47, 48, 164, 165, 166, 167, 270, 271, 272, 273},
                        {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}},
                        {{2, 5, 49, 50, 168, 169, 170, 171, 238, 239, 240, 241},
                        {8, 11, 51, 52, 172, 173, 174, 175, 250, 251, 252, 253},
                        {23, 26, 53, 54, 176, 177, 178, 179, 262, 263, 264, 265},
                        {29, 32, 55, 56, 180, 181, 182, 183, 274, 275, 276, 277},
                        {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}}};
                        int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
                        int skhv1 = 15;// ti le
                        int skhv2 = 25;//ti le
                        int skhc = 40;//ti le
                        int skhId = -1;
                        int rd = Util.nextInt(1, 100);
                        if (rd <= skhv1) {
                            skhId = 0;
                        } else if (rd <= skhv1 + skhv2) {
                            skhId = 1;
                        } else if (rd <= skhv1 + skhv2 + skhc) {
                            skhId = 2;
                        }
                        Item item = null;
                        switch (player.gender) {
                            case 0:
                                Random rand0 = new Random();
                                int i0 = rand0.nextInt(items.length);
                                int j0 = rand0.nextInt(items[0].length);
                                int k0 = rand0.nextInt(items[0][item1.template.type].length);
                                int randomThirdElement0 = items[0][item1.template.type][k0];
                                item = ItemService.gI().itemSKH(randomThirdElement0, options[0][skhId]);
                                break;
                            case 1:
                                Random rand1 = new Random();
                                int i1 = rand1.nextInt(items.length);
                                int j1 = rand1.nextInt(items[1].length);
                                int k1 = rand1.nextInt(items[1][item1.template.type].length);
                                int randomThirdElement1 = items[1][item1.template.type][k1];
                                item = ItemService.gI().itemSKH(randomThirdElement1, options[1][skhId]);
                                break;
                            case 2:
                                Random rand2 = new Random();
                                int i2 = rand2.nextInt(items.length);
                                int j2 = rand2.nextInt(items[2].length);
                                int k2 = rand2.nextInt(items[2][item1.template.type].length);
                                int randomThirdElement2 = items[2][item1.template.type][k2];
                                item = ItemService.gI().itemSKH(randomThirdElement2, options[2][skhId]);
                                break;
                        }
                        if (item != null) {
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().addItemBag(player, item);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                            InventoryServiceNew.gI().sendItemBags(player);
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                    player.combineNew.itemsCombine.clear();
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Cần 1 o tromg trong hanh trang");
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);

            } else {
                Service.getInstance().sendThongBao(player, "Cần 3 món huỷ diệt cùng loại");
            }
        }
    }

//    public void huyDietLenKichHoatThuong(Player player) {
//        //  món đầu  làm gốc
//        if (player.combineNew.itemsCombine.size() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
//            return;
//        }
//        Item item1 = player.combineNew.itemsCombine.get(0);
//        if (item1.isDHD()) {
//            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                if (player.inventory.gold < COST) {
//                    Service.getInstance().sendThongBao(player, "Con cần thêm vàng để đổi...");
//                    return;
//                }
//                player.inventory.gold -= COST;
//                int type = item1.template.type;
//                int[][][] items = {{{0, 3, 33, 34, 136, 137, 138, 139, 230, 231, 232, 233},// áo
//                {6, 9, 35, 36, 140, 141, 142, 143, 242, 243, 244, 245}, // quần
//                {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257}, // găng
//                {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269}, // giày
//                {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}}, // rada
//
//                {{1, 4, 41, 42, 152, 153, 154, 155, 234, 235, 236, 237},
//                {7, 10, 43, 44, 156, 157, 158, 159, 246, 247, 248, 249},
//                {22, 25, 45, 46, 160, 161, 162, 163, 258, 259, 260, 261},
//                {28, 31, 47, 48, 164, 165, 166, 167, 270, 271, 272, 273},
//                {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}},
//                {{2, 5, 49, 50, 168, 169, 170, 171, 238, 239, 240, 241},
//                {8, 11, 51, 52, 172, 173, 174, 175, 250, 251, 252, 253},
//                {23, 26, 53, 54, 176, 177, 178, 179, 262, 263, 264, 265},
//                {29, 32, 55, 56, 180, 181, 182, 183, 274, 275, 276, 277},
//                {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}}};
//                int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
//                int skhv1 = 25;// ti le
//                int skhv2 = 35;//ti le
//                int skhc = 40;//ti le
//                int skhId = -1;
//                int rd = Util.nextInt(1, 100);
//                if (rd <= skhv1) {
//                    skhId = 0;
//                } else if (rd <= skhv1 + skhv2) {
//                    skhId = 1;
//                } else if (rd <= skhv1 + skhv2 + skhc) {
//                    skhId = 2;
//                }
//                Item item = null;
//                switch (player.gender) {
//                    case 0:
//                        Random rand0 = new Random();
//                        int i0 = rand0.nextInt(items.length);
//                        int j0 = rand0.nextInt(items[0].length);
//                        int k0 = rand0.nextInt(items[0][item1.template.type].length);
//                        int randomThirdElement0 = items[0][item1.template.type][k0];
//                        item = ItemService.gI().itemSKH(randomThirdElement0, options[0][skhId]);
//                        break;
//                    case 1:
//                        Random rand1 = new Random();
//                        int i1 = rand1.nextInt(items.length);
//                        int j1 = rand1.nextInt(items[1].length);
//                        int k1 = rand1.nextInt(items[1][item1.template.type].length);
//                        int randomThirdElement1 = items[1][item1.template.type][k1];
//                        item = ItemService.gI().itemSKH(randomThirdElement1, options[1][skhId]);
//                        break;
//                    case 2:
//                        Random rand2 = new Random();
//                        int i2 = rand2.nextInt(items.length);
//                        int j2 = rand2.nextInt(items[2].length);
//                        int k2 = rand2.nextInt(items[2][item1.template.type].length);
//                        int randomThirdElement2 = items[2][item1.template.type][k2];
//                        item = ItemService.gI().itemSKH(randomThirdElement2, options[2][skhId]);
//                        break;
//                }
//                if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                    InventoryServiceNew.gI().addItemBag(player, item);
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
//                    InventoryServiceNew.gI().sendItemBags(player);
//                } else {
//                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
//                }
//            }
//            InventoryServiceNew.gI().sendItemBags(player);
//            Service.getInstance().sendMoney(player);
//            player.combineNew.itemsCombine.clear();
//            reOpenItemCombine(player);
//        } else {
//            Service.getInstance().sendThongBao(player, "Cần 1 món huỷ diệt");
//        }
//    }
    public void huyDietLenKichHoatThuong(Player player) {
        //  món đầu  làm gốc
        if (player.combineNew.itemsCombine.size() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        Item item1 = player.combineNew.itemsCombine.get(0);
        if (item1.isDHD()) {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                if (player.inventory.gold < COST) {
                    Service.getInstance().sendThongBao(player, "Con cần thêm vàng để đổi...");
                    return;
                }
                player.inventory.gold -= COST;
                int type = item1.template.type;
                int[][] items = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}};
                int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
                int skhv1 = 25;// ti le
                int skhv2 = 35;//ti le
                int skhc = 40;//ti le
                int skhId = -1;
                int rd = Util.nextInt(1, 100);
                if (rd <= skhv1) {
                    skhId = 0;
                } else if (rd <= skhv1 + skhv2) {
                    skhId = 1;
                } else if (rd <= skhv1 + skhv2 + skhc) {
                    skhId = 2;
                }
                Item item = null;
                switch (player.gender) {
                    case 0:
                        item = ItemService.gI().itemSKH(items[0][item1.template.type], options[0][skhId]);
                        break;
                    case 1:
                        item = ItemService.gI().itemSKH(items[1][item1.template.type], options[1][skhId]);
                        break;
                    case 2:
                        item = ItemService.gI().itemSKH(items[2][item1.template.type], options[2][skhId]);
                        break;
                }
                if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
                }
            }
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Cần 1 món huỷ diệt");
        }
    }

    private void dapDoKichHoat2(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dhd = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 650 && item.template.id <= 662) {
                        dhd = item;
                    } else if (item.template.id >= 555 && item.template.id <= 567) {
                        dtl = item;
                    }
                }
            }
            if (dhd != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tiLe = dtl != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0(dhd.template.gender, dhd.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dhd, 1);
                    if (dtl != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapThanLinhLenHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item dtl = null;
            dtl = player.combineNew.itemsCombine.get(0);
            if (dtl != null && dtl.isDTL()) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tiLe =  50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        ItemService.gI().OpenDHD2(player, player.gender, dtl.template.type);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }
    private void nangcapts(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item dhd = null;
            Item manhts = null;
            Item dts = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (dhd != null && dhd.isDHD()) {
                            dhd = item;
                } else if (manhts != null && manhts.isManhTS()) {
                    manhts = item;
                } else if (dts != null && manhts.isDTS()) {
                    dts = item;    
                }
            }
            if (dhd != null && manhts != null && manhts.quantity >= 999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhts, 999);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                ItemService.gI().OpenDoTs(player, player.gender, dts.template.type);
                InventoryServiceNew.gI().subQuantityItemsBag(player, dts, 1);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void doiVeHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
                    ticket.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().addItemBag(player, ticket);
                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }
    

    private void moChiSolinhthu(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item manhHon = null;
            Item daXanhLam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id >= 2019 && item.template.id <= 2026) {
                    linhthu = item;
                } else if (item.template.id == 2030) {
                    manhHon = item;
                } else if (item.template.id >= 663 && item.template.id <= 667) {
                    daXanhLam = item;
                }
            }
            if (linhthu != null && manhHon != null && manhHon.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhHon, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daXanhLam, 50);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 45)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 45)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 45)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(15, 45)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(15, 45)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(3, 10)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(15, 45)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(15, 45)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void moChiSoPET(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item manhHon = null;
            Item daXanhLam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id >= 1145 && item.template.id <= 2026) {
                    linhthu = item;
                } else if (item.template.id == 2030) {
                    manhHon = item;
                } else if (item.template.id >= 663 && item.template.id <= 667) {
                    daXanhLam = item;
                }
            }
            if (linhthu != null && daXanhLam != null && manhHon != null && daXanhLam.quantity >= 50 && manhHon.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhHon, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daXanhLam, 50);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 45)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 45)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 45)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(15, 45)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(15, 45)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(3, 10)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(15, 45)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(15, 45)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int ruby = player.combineNew.gemCombine;
            if (player.inventory.ruby < ruby) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int starEmpty = 0; //lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.ruby -= ruby;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                    }

                    InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player) {
       boolean flag = false;
        int solandap = player.combineNew.quantities;
        while (player.combineNew.quantities > 0 && !player.combineNew.itemsCombine.isEmpty() && !flag) {
            int gold = player.combineNew.goldCombine;
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                break;
            } else if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                break;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.ruby -= ruby;
                    //float ratio = getRatioPhaLeHoa(star);
                    int epint = (int) getRatioPhaLeHoa(star);
                    flag = Util.isTrue(epint,100);
                    if (flag) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        Service.getInstance().sendThongBao(player, "Lên cấp sau " + (solandap - player.combineNew.quantities + 1) + " lần đập");
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
            }
            player.combineNew.quantities -= 1;          
        }
        if (!flag) {
            sendEffectFailCombine(player);
        }
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }
    
    private void phaLeHoaCaiTrang(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isCaitrang(item)) {
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void buff(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.rubyCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.ruby < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 34) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_TINH_AN) {
                    player.inventory.gold -= gold;
                    player.inventory.ruby -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(34, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 1) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
                                    + "thành công " + item.template.name + " lên " + optionStar.param);
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                    sendEffectCombineDB(player, item.template.iconID);
                }
            }
        }
    }

    //    private void phanradothanlinh(Player player) {
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            if (!player.combineNew.itemsCombine.isEmpty()) {
//                Item item = player.combineNew.itemsCombine.get(0);
//                if (item != null && item.isNotNullItem() && (item.template.id > 0 && item.template.id <= 3) && item.quantity >= 1) {
//                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 78));
//                    InventoryServiceNew.gI().addItemBag(player, nr);
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
//                    Service.getInstance().sendThongBao(player, "Đã nhận được 1 điểm");
//
//                }
//            }
//        }
//    }
    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
//                        if (optionLevel != null && optionLevel.param >= 5) {
//                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
//                                    + "thành công " + trangBi.template.name + " lên +" + optionLevel.param);
//                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 10 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * r
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //--------------------------------------------------------------------------Ratio, cost combine
    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 50000000;
            case 1:
                return 1000000;
            case 2:
                return 2000000;
            case 3:
                return 4000000;
            case 4:
                return 6000000;
            case 5:
                return 9000000;
            case 6:
                return 50000000;
            case 7:
                return 100000;
            case 8:
                return 2000000;

        }
        return 0;
    }
    private int getGoldPhaLeHoa1(int star) {
        switch (star) {
            case 0:
                return 10000000;
            case 1:
                return 20000000;
            case 2:
                return 40000000;
            case 3:
                return 80000000;
            case 4:
                return 160000000;
            case 5:
                return 320000000;
            case 6:
                return 400000000;
            case 7:
                return 500000000;
            case 8:
                return 600000000;

        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 40;
            case 3:
                return 35;
            case 4:
                return 30;
            case 5:
                return 25;
            case 6:
                return 5;
            case 7: // 7 sao
                return 1;
            case 8:
                return 1;
            case 9:
                return 1;
        }

        return 0;
    }
    private float getRatioPhaLeHoa1(int star) {
        switch (star) {
            case 0:
                return 100f;
            case 1:
                return 90f;
            case 2:
                return 80f;
            case 3:
                return 60f;
            case 4:
                return 50f;
            case 5:
                return 40f;
            case 6:
                return 20f;
            case 7: // 7 sao
                return 10f;
            case 8:
                return 5f;
        }

        return 0;
    }

    private int getRubyPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 100;
            case 8:
                return 200;
            case 9:
                return 10000;
        }
        return 0;
    }

    private int getGoldTinhAn(int star) {
        switch (star) {
            case 0:
                return 500000000;
        }
        return 0;
    }

    private float getRatioTinhAn(int star) { //tile dap do chi hat mit
        switch (star) {
            case 0:
                return 5f;
        }
        return 0;
    }

    private int getGemRuby(int star) {
        switch (star) {
            case 0:
                return 10;
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 100;
        }
        return 0;
    }

    private double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 7;
            case 5:
                return 5;
            case 6:
                return 1;
            case 7: // 7 sao
                return 0.3;
            case 8:
                return 0.3;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 70;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 70000;
            case 2:
                return 300000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 100000000;
            case 7:
                return 250000000;
        }
        return 0;
    }

    //--------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20));
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 5 || item.template.type == 32) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    private boolean isCaitrang(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type == 11 || item.template.type == 5 || item.template.type == 27 || item.template.type == 23) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5; // +5%hp
            case 19:
                return 5; // +5%ki
            case 18:
                return 5; // +5%hp/30s
            case 17:
                return 5; // +5%ki/30s
            case 16:
                return 3; // +3%sđ
            case 15:
                return 2; // +2%giáp
            case 14:
                return 2; // +2%né đòn
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 50;
            case 15:
                return 94;
            case 14:
                return 108;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case PHA_LE_HOA_CAI_TRANG:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành cai trang pha lê";    
            case PHA_LE_HOA_TINH_AN:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị tinh ấn";
            case NANG_TL_LEN_HUY_DIET:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị Hủy Diệt";  
            case NANG_HUY_DIET_LEN_SKH:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị Kích Hoạt";
            case NANG_HUY_DIET_LEN_SKH_VIP:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị Kích Hoạt VIP";     
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2";
            case NANG_CAP_LINH_THU:
                return "Ta sẽ phù phép\ncho Linh Thú cùi bắp của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_PET:
                return "Ta sẽ phù phép\ncho PET cùi bắp của ngươi\ncó 1 chỉ số ngẫu nhiên";    
            case NANG_CAP_PT_skH:
                return "Ta sẽ phù phép\ncho SKH tang them chi so";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_BONG_TAI2:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 3";
            case MO_CHI_SO_BONG_TAI2:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 3 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case PHAN_RA_DO_THAN_LINH:
                return "Ta sẽ phân rã \n  trang bị của người thành điểm!";
            case NANG_CAP_DO_TS:
                return "Ta sẽ nâng cấp \n  trang bị của người thành\n đồ thiên sứ!";
            case NANG_CAP_SKH_VIP:
                return "Thiên sứ nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_HUY_DIET_LEN_SKH_VIP:
                return "Chọn trang bị\n(3 Món Hủy Diệt Khác Loại)\n"
                        + "Sau đó chọn 'Nâng cấp'";    
            case NANG_HUY_DIET_LEN_SKH:
                return "Chọn trang bị\n(1 Món Hủy Diệt Để Nâng Cấp Lên Đồ Kích Hoạt)\n"
                        + "Sau đó chọn 'Nâng cấp'"; 
            case NANG_TL_LEN_HUY_DIET:
                return "Chọn trang bị\n(1 món Thân Linh Để Nâng Cấp Lên Đồ Hủy Diệt)\n"
                        + "Sau đó chọn 'Nâng cấp'";  
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_CAI_TRANG:
                return "Chọn trang bị\n(VPDL, PET, LINH THU, VAN BAY, CAI TRANG)\nSau đó chọn 'Nâng cấp'";    
            case PHA_LE_HOA_TINH_AN:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\n9999 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh hồn bông tai số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI2:
                return "Vào hành trang\nChọn bông tai Porata C2\nChọn mảnh bông tai để nâng cấp, số lượng\n9999 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI2:
                return "Vào hành trang\nChọn bông tai Porata C2\nChọn mảnh hồn bông tai số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case PHAN_RA_DO_THAN_LINH:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để phân rã\n"
                        + "Sau đó chọn 'Phân Rã'";
            case NANG_CAP_DO_TS:
                return "vào hành trang\nChọn 2 trang bị hủy diệt bất kì\nkèm 1 món đồ thần linh\n và 99 mảnh thiên sứ\n "
                        + "sẽ cho ra đồ thiên sứ từ 0-15% chỉ số"
                        + "Sau đó chọn 'Nâng Cấp'";
            case NANG_CAP_SKH_VIP:
                return "vào hành trang\nChọn 1 trang bị thiên sứ bất kì\nChọn tiếp ngẫu nhiên 2 món SKH thường \n "
                        + " đồ SKH VIP sẽ cùng loại \n với đồ thiên sứ!"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case NANG_CAP_LINH_THU:
                return "Vào hành trang\nChọn Linh Thú\nChọn Thăng Tinh Thạch số lượng x99 \nvà x50 Thức Ăn để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_PET:
                return "Vào hành trang\nChọn PET\nChọn Thăng Tinh Thạch số lượng x99 \nvà x50 Thức Ăn để nâng cấp\nSau đó chọn 'Nâng cấp'";    
            case NANG_CAP_PT_skH:
                return "Vào hành trang\nChọnitem kich hoat\nChọn item than linh\nSau đó chọn 'Nâng cấp'";
            default:
                return "";
        }
    }

}