package com.girlkun.models.npc;

import com.girlkun.consts.ConstMap;
import com.girlkun.models.boss.list_boss.nappa.Kuku;
import com.girlkun.server.ServerManager;
import com.girlkun.server.io.MySession;
import com.girlkun.services.*;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.list_boss.NhanBan;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.clan.ClanMember;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.SummonDragon;

import static com.girlkun.services.func.SummonDragon.SHENRON_1_STAR_WISHES_1;
import static com.girlkun.services.func.SummonDragon.NRHD_STAR_WISHES_1;
import static com.girlkun.services.func.SummonDragon.SHENRON_1_STAR_WISHES_2;
import static com.girlkun.services.func.SummonDragon.NRHD_STAR_WISHES_1;
import static com.girlkun.services.func.SummonDragon.SHENRON_SAY;
import static com.girlkun.services.func.SummonDragon.NRHD_SAY;

import com.girlkun.models.player.Player;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.map.bando.BanDoKhoBau;
import com.girlkun.models.map.bando.BanDoKhoBauService;
import com.girlkun.models.map.doanhtrai.DoanhTrai;
import com.girlkun.models.map.doanhtrai.DoanhTraiService;
import com.girlkun.models.map.gas.Gas;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.NPoint;
import com.girlkun.models.matches.PVPService;
import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.services.func.CombineServiceNew;
import com.girlkun.services.func.Input;
import com.girlkun.services.func.LuckyRound;
import com.girlkun.services.func.TopService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import com.girlkun.services.func.ChonAiDay;
import static com.girlkun.services.func.CombineServiceNew.NANG_CAP_LINH_THU;
import static com.girlkun.services.func.CombineServiceNew.NANG_CAP_PET;
import com.kygui.ShopKyGuiService;
import java.util.logging.Level;

public class NpcFactory {

    private static final int COST_HD = 50000000;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    //playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private static Npc npc28(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private NpcFactory() {

    }

    private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Trứng Linh thú cần:\b|7|X99 Hồn Linh Thú + 1 Tỷ vàng", "Đổi Trứng\nLinh thú", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 200tr vàng không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 200 Triệu vàng ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.getAura(), player.getEffFront()},
                                                player.nPoint.dame,
                                                new int[]{player.nPoint.hpMax},
                                                new int[]{140},
                                                skillTemp,
                                                new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60
                                        );

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //trừ vàng khi gọi boss
                                        player.inventory.gold -= 200_000_000;
                                        Service.getInstance().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Giuma(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 153) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi có muốn tiến vào map để up mảnh vỡ và mảnh hồn bông tai hay không ?", "Dồng Ý", "Đóng");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                   // ChangeMapService.gI().changeMapInYard(player, 157, -1, 552);
                                   // break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.getSession().is_gift_box) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Bãi Biển Kame", "Giải tán bang hội", "Nhận quà\nđền bù");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Bãi Biển Kame", "Giải tán bang hội","Dong Thanh Dia");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                            ChangeMapService.gI().changeMap(player, 172, -1, 869, 408); 
                                break;
                            case 1:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (clan.members.size() > 1) {
                                            Service.getInstance().sendThongBao(player, "Bang phải còn một người");
                                            break;
                                        }
                                        if (!clan.isLeader(player)) {
                                            Service.getInstance().sendThongBao(player, "Phải là bảng chủ");
                                            break;
                                        }
//                                        
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                "Yes you do!", "Từ chối!");
                                    }
                                    break;
                                }
                                Service.getInstance().sendThongBao(player, "Có bang hội đâu ba!!!");
                                break;
                            
                            
                            case 2:
                                if (player.nPoint.power < 60000000000L) {
                                    Service.gI().sendThongBao(player, "Cần đạt tới 60 tỉ sức mạnh mới có thể vào lãnh địa bang hội!!!");
                                    return;
                                } else {
                                    ChangeMapService.gI().changeMapInYard(player, 157, -1, 552);
                                }
                                break;    
                                   

                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        switch (select) {

                            
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                BanDoKhoBauService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                           
                                        
                        }
                    }
                }
            }

        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con cố gắng theo %1 học thành tài, đừng lo lắng cho ta."
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                "Đổi mật khẩu", "Nhận 200k ngọc xanh", "GiftCode", "Nhan de tu");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Input.gI().createFormChangePassword(player);
                                break;
                            case 1:
                                if (player.inventory.gem == 200000) {
                                    this.npcChat(player, "Bú ít thôi con");
                                    break;
                                }
                                player.inventory.gem = 200000;

                                Service.getInstance().sendMoney(player);
                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 200K ngọc xanh");
                                break;
//                            case 2:
//                                if (player.pet == null) { 
//                                    PetService.gI().createMabuPet(player);
//                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được đệ tử");
//                                } else {
//                                    this.npcChat(player, "Bú ít thôi con");
//                                }
//                                break;
                            case 2:
                                if (player.nPoint.power < 8) {
                                    Service.gI().sendThongBao(player, "Phải đủ 0 tỉ sức mạnh trở lên !");
                                    return;
                                } else {
                                    Input.gI().createFormGiftCode(player);
                                }
                                break;
                            case 3:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                } else {
                                    this.npcChat("Con đã nhận đệ tử rồi");
                                }
//                                PetService.gI().createNormalPet(player, player.gender);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.QUA_TAN_THU) {
                        switch (select) {
                            case 0:
//                                        if (!player.gift.gemTanThu) {
                                if (true) {
                                    player.inventory.gem = 100000;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 100K ngọc xanh");
                                    player.gift.gemTanThu = true;
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con đã nhận phần quà này rồi mà",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (nhanVang) {
                                    player.inventory.gold = Inventory.LIMIT_GOLD;
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 2 tỉ vàng");
                                } else {
                                    this.npcChat("");
                                }
                                break;
                            case 2:
                                if (nhanDeTu) {
                                    if (player.pet == null) {
                                        PetService.gI().createMabuPet(player);
                                        Service.getInstance().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                    } else {
                                        this.npcChat("Con đã nhận đệ tử rồi");
                                    }
                                }

                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_THUONG) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "ITEMS_REWARD", true);
                                break;
                            case 1:
                                if (player.getSession().goldBar > 0) {
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        int quantity = player.getSession().goldBar;
                                        Item goldBar = ItemService.gI().createNewItem((short) 2094, quantity);
                                        InventoryServiceNew.gI().addItemBag(player, goldBar);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Ông đã để " + quantity + " thỏi vàng vào hành trang con rồi đấy");
                                        PlayerDAO.subGoldBar(player, quantity);
                                        player.getSession().goldBar = 0;
                                    } else {
                                        this.npcChat(player, "Con phải có ít nhất 1 ô trống trong hành trang ông mới đưa cho con được");
                                    }
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_THE) {
                        Input.gI().createFormNapThe(player, (byte) select);
                    }
                }
            }
        };
    }
     public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đưa cho ta thỏi vàng và ngươi sẽ giúp ngươi làm giàu\nCá cược Tài Xỉu không bao giờ chán!!!\b|7|Thắng ngươi sẽ được x1.8 lần số Hồng ngọc cược\nThua thì còn cái nịt haha\b|5| Điều kiện được tham gia: Nhiệm vụ 24 trở lên\b|4|(Cược tối thiểu 10.000 Hồng ngọc)",
                            "Xỉu", "Tài");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                        Input.gI().XIU(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
                                    }
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                        Input.gI().TAI(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
                                    }
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                    }
                }
            }
    
    
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda" );
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị","VamChar");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                            case 3:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 124);
                                break;    
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị","VamChar");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                            case 3:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 124);
                                break;    
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(pl);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                        + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị","VamChar");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                                case 3:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 124);
                                break;    
                            }
                        }
                    }
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_23_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                        break;
                                    }
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_18_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                        break;
                                    }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Boss đã bị tiêu diệt vui lòng chờ!");
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_23_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                        break;
                                    }
                                case 2:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_18_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                        break;
                                    }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Boss đã bị tiêu diệt vui lòng chờ!");
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_23_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                        break;
                                    }
                                case 2:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_18_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                        break;
                                    }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Boss đã bị tiêu diệt vui lòng chờ!");
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_23_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                        break;
                                    }
                                case 2:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_18_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                        break;
                                    }
                            }
                        }
                    }
                    if (this.mapId == 68) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng","Xem Boss","Khu Rừng Băng Giá");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
//                                    case 2: //shop
//                                    ShopServiceNew.gI().opendShop(player, "HALLOWEEN", false);
//                                    break;
                                     case 2:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_28_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 205, -1, -1);
                                        break;
                                    }
                               case 1: {                                    
                                BossManager.gI().showListBosspl(player);                               
                                break;
                                }
                                //case 1: //tiệm hồng ngọc
                                //    ShopServiceNew.gI().opendShop(player, "SHOP_HUNG_VUONG", false);
                                //    break;
                                //case 2:
//                                    if (player.getSession().actived) {
//                                    ShopServiceNew.gI().opendShop(player, "SANTA_EVENT", false);
//                                    } 
//                                    else {
//                                        Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    }
                                    //break;

//                                case 2: //tiệm hớt tóc
//                                    ShopServiceNew.gI().opendShop(player, "SANTA_HEAD", false);
//                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
    public static Npc jiren(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|Khuyến mãi nạp ngọc x2\n|2|10k = 10k hồng ngọc\n50k = 65k hồng ngọc \n 100k = 140k hồng ngọc \nXin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng","Quy Đổi\nCải Trang");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "jiren", false);
                                    break;
                                case 1:
                                        Item hcd = null;
//                                    Item hcb = null;
                                    Item hcv = null;
//                                    Item duahau = null;
//                                    Item carot = null;
                                    try {
                                        hcd = InventoryServiceNew.gI().findItemBag(player, 2030);
                                        hcv = InventoryServiceNew.gI().findItemBag(player, 1176);
//                                        hcb = InventoryServiceNew.gI().findItemBag(player, 981);
//                                        duahau = InventoryServiceNew.gI().findItemBag(player, 569);
//                                        carot = InventoryServiceNew.gI().findItemBag(player, 462);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (hcd == null || hcd.quantity < 99 && hcv == null || hcv.quantity < 99 ) {
                                        this.npcChat(player, "Yêu Cầu Có X99 đá ma thuật Và X99 Mảnh Giấy");
                                    } else if (player.inventory.ruby < 1000) {
                                        this.npcChat(player, "Bạn không đủ 1k hồng ngọc");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.ruby -= 1000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hcd, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hcv, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hcb, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 10);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, carot, 5);
                                        Service.getInstance().sendMoney(player);
                                        Item than = ItemService.gI().createNewItem((short) 1129);
                                        than.itemOptions.add(new Item.ItemOption(50, new Random().nextInt(15) + 15));//hsd
                                        than.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(15) + 15));//hsd
                                        than.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(15) + 15));//hsd
                                        than.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(1) + 4));//hsd
//                                        than.itemOptions.add(new Item.ItemOption(117, 30));//ki
//                                        than.itemOptions.add(new Item.ItemOption(106, 1));//
                                        than.itemOptions.add(new Item.ItemOption(5, new Random().nextInt(1) + 9));//hsd
                                        if (Util.isTrue(299, 300)) {// tỉ lệ ra hsd
                                            than.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 7));//hsd
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, than);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Đổi Cải Trang Thành Công");
                                    }
                                    break;
                                        
                               case 2: {                                    
                                BossManager.gI().showListBosspl(player);                               
                                break;
                                }
                                //case 1: //tiệm hồng ngọc
                                //    ShopServiceNew.gI().opendShop(player, "SHOP_HUNG_VUONG", false);
                                //    break;
                                //case 2:
//                                    if (player.getSession().actived) {
//                                    ShopServiceNew.gI().opendShop(player, "SANTA_EVENT", false);
//                                    } 
//                                    else {
//                                        Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    }
                                    //break;

//                                case 2: //tiệm hớt tóc
//                                    ShopServiceNew.gI().opendShop(player, "SANTA_HEAD", false);
//                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
    

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị", //1
                                "Pha lê\nhóa\ntrang bị",//2
//                                "Pha lê\nhóa\nCAI TRANG",
                                "Đập Đồ Huỷ Diệt"); //5
                               
//                                "Đập đồ kích hoạt vip");//7
//                                "Nâng Cấp Linh Thú",
//                                "Nâng Cấp Pet"
                                
                                
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nâng cấp\nBông tai\nPorata", "Làm phép\nNhập đá",
                                "Nhập\nNgọc Rồng","Mở chỉ số Bông tai Porata Cấp 2", "Nâng cấp Bông tai Porata C3", "Mở chỉ Số Bông tai Cấp 3", "Nâng cấp Bông tai Porata C4", "Mở chỉ Số Bông tai Cấp 4");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
//                                                CombineService.gI().openTabCombine(player, CombineService.EP_SAO_TRANG_BI);
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
//                                case 2:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_CAI_TRANG);
//                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_TL_LEN_HUY_DIET);
                                    break;
                                
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_HUY_DIET_LEN_SKH_VIP);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_LINH_THU);
                                    break;
                                case 6:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_PET);
                                    break;     
                                     
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_CAI_TRANG: 
                                case CombineServiceNew.PHA_LE_HOA_TINH_AN:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_PT_skH:
                                case CombineServiceNew.NANG_TL_LEN_HUY_DIET:
                                case CombineServiceNew.NANG_HUY_DIET_LEN_SKH:
                                case CombineServiceNew.NANG_HUY_DIET_LEN_SKH_VIP:
                                case CombineServiceNew.NANG_CAP_LINH_THU:
                                case CombineServiceNew.NANG_CAP_PET:  
                                    
                                    
                                  
                                    switch (select) {
                                        case 0:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1;
                                            }
                                            break;
                                        case 1:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 10;
                                            }
                                            break;
                                        case 2:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 100;
                                            }
                                            break;      
                                    }
                                        CombineServiceNew.gI().startCombine(player);
                            
                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    break;
                                case 1:
//                                                CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_TRANG_BI);
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 3: //làm phép nhập đá
                                    break;
                                case 4:
//                                                CombineService.gI().openTabCombine(player, CombineService.NHAP_NGOC_RONG);
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;
                                case 5:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                                case 6:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI2);
                                    break;
                                case 7:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI2);
                                    break;
                                case 8:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI3);
                                    break;
                                case 9:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI3);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.LAM_PHEP_NHAP_DA:
                                case CombineServiceNew.NHAP_NGOC_RONG: 
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NANG_CAP_BONG_TAI2:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI2:
                                case CombineServiceNew.NANG_CAP_BONG_TAI3:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI3:

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    }
                }
            }
        };
    }
     public static Npc bulmapt(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
//                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "Chào bạn tôi sẽ đưa bạn đến hành tinh Ma quái?\n"
                                        + "Yêu cầu 150Tỉ để nhận được mốc sức mạnh","Nhận Quà Mốc\nSức Mạnh","Nhận Quà\nTri Ân","Cửa Hàng");
                    }
                    if (this.mapId == 204) {
                        this.createOtherMenu(player, 0,
                               "|7| Đổi vật Phẩm cần:\n x99 Mảnh đầu lâu + x2 Công thức Halloween + 2k hồng ngọc\n Đánh Quái tại đây sẽ nhận được mảnh đầu lâu\n Bên trong Hòm Halloween có chứa vật phẩm đeo lưng + Linh Thú", "Về\n Đảo Kame", "Đổi \nVật Phẩm");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 2: //shop
                                    ShopServiceNew.gI().opendShop(player, "new", false);
                                    break;
                                case 3:
                                    if (player.getSession().player.nPoint.power >= 90000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 204, -1, -1);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 90 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai
                                case 0: {
                                    
                                    if (player.getSession().player.nPoint.power < 150000000000L){
                                        this.npcChat(player, "Bạn không đủ điều kiện nhận Cải Trang");
                                        }
                                    else{                                                                           
                                        Item danhhieu = ItemService.gI().createNewItem((short) 2095);
                                        danhhieu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20,30)));
                                        danhhieu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25,30)));
                                        danhhieu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25,30)));
                                        danhhieu.itemOptions.add(new Item.ItemOption(95, Util.nextInt(5,10)));
                                        danhhieu.itemOptions.add(new Item.ItemOption(96, Util.nextInt(5,10)));
                                        if (Util.isTrue(299, 300)) {// tỉ lệ ra hsd
                                            danhhieu.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 7));//hsd
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, danhhieu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được Cải Trang");
                                    }
                                break;
                                }
                                case 1: {
                                    
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_25_3) {
                                        this.npcChat(player, "Yêu cầu làm qua nhiệm vụ Xên Con Cấp 4");
                                        }
                                    else{                                                                           
                                        Item danhhieu = ItemService.gI().createNewItem((short) 1207);
                                        danhhieu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1,15)));
                                        danhhieu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1,15)));
                                        danhhieu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1,15)));
                                        danhhieu.itemOptions.add(new Item.ItemOption(5, Util.nextInt(1,10)));
                                        danhhieu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1,10)));
                                        if (Util.isTrue(299, 300)) {// tỉ lệ ra hsd
                                            danhhieu.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(1) + 2));//hsd
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, danhhieu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được Xe bí");
                                    }
                                break;
                                }
                            }
                        }
                    }
                    if (this.mapId == 204) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, -1);
                                    break; // quay ve
                                    case 1:
                                        Item hcd = null;
//                                    Item hcb = null;
                                    Item hcv = null;
//                                    Item duahau = null;
//                                    Item carot = null;
                                    try {
                                        hcd = InventoryServiceNew.gI().findItemBag(player, 1202);
                                        hcv = InventoryServiceNew.gI().findItemBag(player, 1203);
//                                        hcb = InventoryServiceNew.gI().findItemBag(player, 981);
//                                        duahau = InventoryServiceNew.gI().findItemBag(player, 569);
//                                        carot = InventoryServiceNew.gI().findItemBag(player, 462);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (hcd == null || hcd.quantity < 99 && hcv == null || hcv.quantity < 2 ) {
                                        this.npcChat(player, "Yêu Cầu Có X99 Mảnh đầu lâu Và x2 Công thức");
                                    } else if (player.inventory.ruby < 2000) {
                                        this.npcChat(player, "Bạn không đủ 2k hồng ngọc");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.ruby -= 2000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hcd, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hcv, 2);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hcb, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 10);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, carot, 5);
                                        Service.getInstance().sendMoney(player);
                                        Item than = ItemService.gI().createNewItem((short) 1199);
                                        than.itemOptions.add(new Item.ItemOption(30, 0));//hsd
                                        than.itemOptions.add(new Item.ItemOption(87,0));//hsd
//                                        than.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(15) + 15));//hsd
//                                        than.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(1) + 4));//hsd
//                                        than.itemOptions.add(new Item.ItemOption(117, 30));//ki
//                                        than.itemOptions.add(new Item.ItemOption(106, 1));//
                                        than.itemOptions.add(new Item.ItemOption(5, new Random().nextInt(1) + 9));//hsd
                                        if (Util.isTrue(299, 300)) {// tỉ lệ ra hsd
                                            than.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 7));//hsd
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, than);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Đổi Vật Phẩm Thành Công");
                                    }
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }
    public static Npc thiensu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                
                               
                                "Đập Đồ Thiên Sứ");
                    

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_TS);
                                    break;
                                
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_DO_TS:
                                
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                                    }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                           
                        
                    
                            
                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x2 Tnsm\nHỗ trợ cho Ae Từ\b|1|Dưới 1tr5 SM dến 16 Tỷ SM?", "OK", "Oéo");
                    }
                    if (mapId == 123) {
                        this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Ảru?", "OK", "Từ chối");

                    }
                    if (mapId == 122) {
//                     this.createOtherMenu(player, 0, "Xia xia thua phùa\b|7|Thí chủ đang có: " + player.NguHanhSonPoint +" điểm ngũ hành sơn\b|1|Thí chủ muốn đổi cải trang x4 chưởng ko?", "Âu kê","Top Ngu Hanh Son" , "No");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 0) {
                                if (player.nPoint.power < 1500000 || player.nPoint.power >= 16000000000L) {
                                    Service.getInstance().sendThongBao(player, "Sức mạnh bạn không phù hợp để qua map!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapInYard(player, 123, -1, -1);
                            }
                            if (mapId == 123) {
                                ChangeMapService.gI().changeMapInYard(player, 0, -1, -1);
                            }
                            if (mapId == 122) {
                                if (select == 0) {
                                    if (player.inventory.gold >= 500) {
                                        player.inventory.gold -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (711));
                                        item.itemOptions.add(new Item.ItemOption(49, 80));
                                        item.itemOptions.add(new Item.ItemOption(77, 80));
                                        item.itemOptions.add(new Item.ItemOption(103, 50));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.getInstance().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ điểm, bạn còn " + (500 - player.pointPvp) + " điểm nữa");
                                    }
                                } else if (select == 1) {
//                                Util.showListTop(player, (byte) 4
//                                );
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    TaskService.gI().checkDoneTaskConfirmMenuNpc(player, this, (byte) select);
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.getInstance().hideWaitDialog(player);
                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.getInstance().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.getInstance().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai
//                                    changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            ChangeMapService.gI().goToTuongLai(player);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Potafeu has been slain!", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //   đến potaufeu
                                ChangeMapService.gI().goToPotaufeu(player);
                            }
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

//public static Npc Potage(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 149) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "tét", "Gọi nhân bản");
//                    }
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                   if (select == 0){
//                        BossManager.gI().createBoss(-214);
//                   }
//                }
//            }
//        };
//    }
    public static Npc noibanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14 || this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|Hiện Tại Bạn Đang Đang Có Số Điểm Sự Kiện Là " + player.NguHanhSonPoint + "\nĐể Nấu Bánh Tét Bạn Cần 80 Thúng Nếp + 80 Thịt Heo + 80 Thúng Đậu Xanh + 80 Lá Dong + 1 Khung Tre + 3 Thỏi Vàng. Bạn Sẽ Nhận Được Bánh Tét Và Điểm Sự Kiện. Ngoài Ra Bạn Có Thể Sử Dụng X99 Nguyên Liệu Mỗi Loại Kết Hợp Với 1 Khung Tre Và 5 Thỏi Vàng Để Nhận Được Bánh Chưng Và Điểm Sự Kiện.", "Nấu\nbánh tét", "Nấu\nbánh chưng", "Nấu thỏi vàng");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    Item thungnep = null;
                                    Item thungdxanh = null;
                                    Item ladong = null;
                                    Item thoivang = null;
                                    Item tre = null;

                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 748);
                                        thungnep = InventoryServiceNew.gI().findItemBag(player, 749);
                                        thungdxanh = InventoryServiceNew.gI().findItemBag(player, 750);
                                        ladong = InventoryServiceNew.gI().findItemBag(player, 751);
                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                        tre = InventoryServiceNew.gI().findItemBag(player, 1035);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 80 || thungnep == null || thungnep.quantity < 80 || thungdxanh == null || thungdxanh.quantity < 80 || ladong == null || ladong.quantity < 80 || tre == null || tre.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                    } else if (thoivang == null || thoivang.quantity < 3) {
                                        this.npcChat(player, "Bạn không đủ thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 80);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungnep, 80);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungdxanh, 80);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, ladong, 80);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 3);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tre, 1);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 752);
                                        player.NguHanhSonPoint += 1;
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 bánh tét");
                                    }
                                    break;
                                }

                                case 1: {
                                    Item honLinhThu = null;
                                    Item thungnep = null;
                                    Item thungdxanh = null;
                                    Item ladong = null;
                                    Item thoivang = null;
                                    Item tre = null;

                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 748);
                                        thungnep = InventoryServiceNew.gI().findItemBag(player, 749);
                                        thungdxanh = InventoryServiceNew.gI().findItemBag(player, 750);
                                        ladong = InventoryServiceNew.gI().findItemBag(player, 751);
                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                        tre = InventoryServiceNew.gI().findItemBag(player, 1035);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99 || thungnep == null || thungnep.quantity < 99 || thungdxanh == null || thungdxanh.quantity < 99 || ladong == null || ladong.quantity < 99 || tre == null || tre.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                    } else if (thoivang == null || thoivang.quantity < 5) {
                                        this.npcChat(player, "Bạn không đủ thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungnep, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungdxanh, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, ladong, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tre, 1);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 753);
                                        player.NguHanhSonPoint += 1;
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 bánh chưng");
                                    }
                                    break;
                                }
                                case 2: {
                                    Item honLinhThu = null;
                                    Item thungnep = null;
                                    Item thungdxanh = null;
                                    Item ladong = null;
                                    Item thoivang = null;

                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 748);
                                        thungnep = InventoryServiceNew.gI().findItemBag(player, 749);
                                        thungdxanh = InventoryServiceNew.gI().findItemBag(player, 750);
                                        ladong = InventoryServiceNew.gI().findItemBag(player, 751);
                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99 || thungnep == null || thungnep.quantity < 99 || thungdxanh == null || thungdxanh.quantity < 99 || ladong == null || ladong.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                    } else if (thoivang == null || thoivang.quantity < 5) {
                                        this.npcChat(player, "Bạn không đủ thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungnep, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungdxanh, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, ladong, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 457, 15);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 15 thỏi vàng");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }
    public static Npc Pic2Mai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 172 ) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|Hiện Tại Bạn Đang Đang Có Số Điểm Sự Kiện Là " + player.NguHanhSonPoint + "\nĐể Đổi Hộp Qùa Sự Kiện Hè Bạn Cần 99 San Hô + 99 Bình Nước + 99 Khúc Gỗ + 1 Que Diêm + 5 Thỏi Vàng. Bạn Sẽ Nhận Được Hộp Qùa VIP Và Điểm Sự Kiện. Ngoài Ra Bạn Có Thể Sử Dụng X99 Nguyên Liệu Mỗi Loại Kết Hợp Với 1 Que Diêm Để Nhận Được Hộp Qùa Thường Và Điểm Sự Kiện.", "Đổi/nHộp Qùa VIP", "Đổi/nHộp Qùa VIP");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 172 ) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item sanho = null;
                                    Item binhnuoc = null;
                                    Item khucgo = null;
                                    Item quediem = null;
                                    Item thoivang = null;
                                    

                                    try {
                                        sanho = InventoryServiceNew.gI().findItemBag(player, 1158);
                                        binhnuoc = InventoryServiceNew.gI().findItemBag(player, 1159);
                                        khucgo = InventoryServiceNew.gI().findItemBag(player, 1160);
                                        quediem = InventoryServiceNew.gI().findItemBag(player, 1161);
                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                        
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (sanho == null || sanho.quantity < 99 || binhnuoc == null || binhnuoc.quantity < 99 || khucgo == null || khucgo.quantity < 99 || quediem == null || quediem.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                    } else if (thoivang == null || thoivang.quantity < 5) {
                                        this.npcChat(player, "Bạn không đủ thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, sanho, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, binhnuoc, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, quediem, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                                        
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 1162);
                                        player.NguHanhSonPoint += 1;
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Hộp Qùa VIP");
                                    }
                                    break;
                                }

                                case 1: {
                                    Item sanho = null;
                                    Item binhnuoc = null;
                                    Item khucgo = null;
                                    Item quediem = null;
                                    

                                    try {
                                        sanho = InventoryServiceNew.gI().findItemBag(player, 1158);
                                        binhnuoc = InventoryServiceNew.gI().findItemBag(player, 1159);
                                        khucgo = InventoryServiceNew.gI().findItemBag(player, 1160);
                                        quediem = InventoryServiceNew.gI().findItemBag(player, 1161);
                                        
                                        
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (sanho == null || sanho.quantity < 99 || binhnuoc == null || binhnuoc.quantity < 99 || khucgo == null || khucgo.quantity < 99 || quediem == null || quediem.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, sanho, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, quediem, 1);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 1163);
                                        player.NguHanhSonPoint += 1;
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, binhnuoc, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, quediem, 1);
                                        Service.getInstance().sendMoney(player);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Hộp Qùa Thường");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ongbut(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Hiện tại ta sẽ bán cải trang cho các ngươi", "Cửa hàng");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "SHOP_HUNG_VUONG", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

//     public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                createOtherMenu(player, 0, "Trò chơi Chọn ai đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy may mắn thì có thể tham gia thử", "Thể lệ", "Chọn\nThỏi vàng");
//            }
//
//            @Override
//            public void confirmMenu(Player pl, int select) {
//                if (canOpenNpc(pl)) {
//                    String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
//                    if (pl.iDMark.getIndexMenu() == 0) {
//                        if (select == 0) {
//                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "Thời gian giữa các giải là 5 phút\nKhi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\nLưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy", "Ok");
//                        } else if (select == 1) {
//                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
//                        }
//                    } else if (pl.iDMark.getIndexMenu() == 1) {
//                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
//                                    break;
//                                case 1: {
//                                    if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                        pl.goldNormar += 20;
//                                        ChonAiDay.gI().goldNormar += 20;
//                                        ChonAiDay.gI().addPlayerNormar(pl);
//                                        createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
//                                    } else {
//                                        Service.getInstance().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                    }
//                                }
//                                break;
//                                case 2: {
//                                   if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 200) {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 200);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                        pl.goldVIP += 200;
//                                        ChonAiDay.gI().goldVip += 200;
//                                        ChonAiDay.gI().addPlayerVIP(pl);
//                                        createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
//                                    } else {
//                                        Service.getInstance().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                    }
//                                }
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
    

    public static Npc hungvuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Bạn Muốn Đổi Mị Nương Ư?", "Đồng ý", "Từ chối");
                }
                if (this.mapId == 124) {
                        this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                             "muốn ra khỏi đây không?\nmuốn mở shop sự kiện không?", "ra khỏi đây", "shop sự kiện"); 
                }                    
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "|7|Mỗi Lần Trao Đổi Sẽ Mất Tất Cả Vật Phẩm",
                                            "Đổi Cải Trang\nMị Nương",
                                            "Shop",
                                            "Từ Chối");
                                    break;
                            }
                                       
                     if (this.mapId == 124) {
                        if (player.iDMark.isBaseMenu()) {                                             
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 5, -1, 933, 192);                                   
                                    break;
                                 case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "hungvuong", false);
                                    break; 
                                } 
                         
                            }
                            
                        }
                        } else if (player.iDMark.getIndexMenu() == 1) { // action đổi dồ húy diệt
                            switch (select) {
                                case 0: // trade
                                    Item hcd = null;
                                    Item hcb = null;
                                    Item hcv = null;
                                    Item duahau = null;
                                    Item carot = null;
                                    try {
                                        hcd = InventoryServiceNew.gI().findItemBag(player, 979);
                                        hcv = InventoryServiceNew.gI().findItemBag(player, 980);
                                        hcb = InventoryServiceNew.gI().findItemBag(player, 981);
                                        duahau = InventoryServiceNew.gI().findItemBag(player, 569);
                                        carot = InventoryServiceNew.gI().findItemBag(player, 462);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (hcd == null || hcd.quantity < 99 && hcv == null || hcv.quantity < 99 && hcb == null || hcb.quantity < 99 && duahau == null || duahau.quantity < 10 && carot == null || carot.quantity < 5) {
                                        this.npcChat(player, "Yêu Cầu Có X99 Ba Loại Huy Chương Và X10 Dưa Hấu Và X5 Củ Cà Rốt");
                                    } else if (player.inventory.gold < 100_000_000) {
                                        this.npcChat(player, "Bạn không đủ 100 Tr vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 100_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hcd, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hcv, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hcb, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, carot, 5);
                                        Service.getInstance().sendMoney(player);
                                        Item than = ItemService.gI().createNewItem((short) 860);
                                        than.itemOptions.add(new Item.ItemOption(50, 50));//sd
                                        than.itemOptions.add(new Item.ItemOption(77, 35));//hp
                                        than.itemOptions.add(new Item.ItemOption(103, 35));//ki
                                        than.itemOptions.add(new Item.ItemOption(14, 10));//ki
                                        than.itemOptions.add(new Item.ItemOption(117, 30));//ki
                                        than.itemOptions.add(new Item.ItemOption(106, 1));//
                                        than.itemOptions.add(new Item.ItemOption(30, 1));
                                        if (Util.isTrue(299, 300)) {// tỉ lệ ra hsd
                                            than.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 7));//hsd
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, than);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Đổi Cải Trang Mị Nương Thành Công");
                                    }
                                    break;
                                    case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "hungvuong", false);
                                    break; 

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio");
                    }
                    if (this.mapId == 0) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?\nCon đang còn : " + player.pointPvp + " điểm PvP Point", "Đến DHVT", "Đổi Cải trang sự kiên", "Top PVP");
                    }
                    if (this.mapId == 129) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?", "Quay ve");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    if (!player.getSession().actived) {
                                        Service.getInstance().sendThongBao(player, "You chưa là cư dân của Jake");
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 129, -1, 354);
                                        Service.getInstance().changeFlag(player, Util.nextInt(8));
                                    }
                                    break; // qua dhvt
                                case 1:  // 
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Mèo Kid Lân với tất cả chỉ số là 30%\n ", "Ok", "Tu choi");
                                    // bat menu doi item
                                    break;

                                case 2:  // 
//                                    Util.showListTop(player, (byte) 3);
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1104));
                                        item.itemOptions.add(new Item.ItemOption(49, 30));
                                        item.itemOptions.add(new Item.ItemOption(77, 30));
                                        item.itemOptions.add(new Item.ItemOption(103, 30));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.getInstance().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 129) {
                        switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 354);
                                break;
                        }
                    }
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                            + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }

                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Di chuyển");
                    }
                    if (this.mapId == 0) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Hiện tại ta sẽ bán cải trang cho các ngươi", "Cửa hàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con\nđường\nrắn độc", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    //con đường rắn độc
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 0) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "SHOP_HUNG_VUONG", false);
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    if (player.nPoint.power < 60000000000L) {
                                        Service.gI().sendThongBao(player, "Đạt đủ 60 tỉ sức mạnh mới có thể vào map ");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                        break;
                                    }
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
//                                    if (!player.getSession().actived) {
//                                        Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                + "Thời gian còn lại là "
                                + TimeUtil.getSecondLeft(player.clan.doanhTrai.getLastTimeOpen(), DoanhTrai.TIME_DOANH_TRAI / 1000)
                                + ". Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clan.haveGoneDoanhTrai) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi trại lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenDoanhTrai, "HH:mm:ss") + " hôm nay. Người mở\n"
                                + "(" + player.clan.playerOpenDoanhTrai + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                            + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.mabuEgg.sendMabuEgg();
                    if (player.mabuEgg.getSecondDone() != 0) {
                        this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                    } else {
                        this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.CAN_NOT_OPEN_EGG:
                            if (select == 0) {
                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                            } else if (select == 1) {
                                if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                    player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                    player.mabuEgg.timeDone = 0;
                                    Service.getInstance().sendMoney(player);
                                    player.mabuEgg.sendMabuEgg();
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn không đủ vàng để thực hiện, còn thiếu "
                                            + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                }
                            }
                            break;
                        case ConstNpc.CAN_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                            "Bạn có chắc chắn cho trứng nở?\n"
                                            + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                            "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                    break;
                                case 1:
                                    player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                    break;
                                case 2:
                                    player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_DESTROY_EGG:
                            if (select == 0) {
                                player.mabuEgg.destroyEgg();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.getInstance().sendMoney(player);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.getInstance().sendMoney(player);
                                }
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                if (!player.getSession().actived) {
                                    Service.gI().sendThongBao(player, "Bạn chưa mở thành viên");
                                    return;
                                }
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Đường đến với ngọc rồng sao đen đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                            + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
//                                if (!player.getSession().actived) {
//                                    Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//
//                                } else
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.getInstance().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
                             "Từ Chối");
                }
            }

            //if (player.inventory.gold < 500000000) {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
//                return;
//            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.getInstance().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.getInstance().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.getInstance().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 147, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        //if (select == 0) {
                            //NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOI_SKH_VIP);
                        //}
                        //if (select == 1) {
                            //CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                        //}

                   // } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                        //if (select == 0) {
                           // CombineServiceNew.gI().startCombine(player);

                        //break;
                       // }
                    }
                }
            }
        };
    }

//            @Override
//         //   public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (player.iDMark.isBaseMenu()) {
//                        switch (select) {
//                            case 0:
//                                if (player.clan != null) {
//                                    if (player.clan.khiGas != null) {
//                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_GAS,
//                                                "Bang hội của con đang đi Khi Gas cấp độ "
//                                                        + player.clan.khiGas.level + "\nCon có muốn đi theo không?",
//                                                "Đồng ý", "Từ chối");
//                                    } else {
//
//                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_GAS,
//                                                "Đây là bản đồ kho báu \nCác con cứ yên tâm lên đường\n"
//                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
//                                                "Chọn\ncấp độ", "Từ chối");
//                                    }
//                                } else {
//                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
//                                }
//                                break;
//                            case 1:
//                                Clan clan = player.clan;
//                                if (clan != null) {
//                                    ClanMember cm = clan.getClanMember((int) player.id);
//                                    if (cm != null) {
//                                        if (clan.members.size() > 1) {
//                                            Service.gI().sendThongBao(player, "Bang phải còn một người");
//                                            break;
//                                        }
//                                        if (!clan.isLeader(player)) {
//                                            Service.gI().sendThongBao(player, "Phải là bảng chủ");
//                                            break;
//                                        }
//                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_GAS) {
//                        switch (select) {
//                            case 0:
//                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
//                                    ChangeMapService.gI().goToGas(player);
//                                } else {
//                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
//                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
//                                }
//                                break;
//
//                        }
//                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_GAS) {
//                        switch (select) {
//                            case 0:
//                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
//                                    Input.gI().createFormChooseLevelGas(player);
//                                } else {
//                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
//                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
//                                }
//                                break;
//                        }
//
//                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCPET_GO_TO_GAS) {
//                        switch (select) {
//                            case 0:
//                                GasService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
//                                break;
//                        
//                        }
//                    }
//                }
//            }
//        };
//    }
    private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.getSession().is_gift_box) {
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Khí Gas Hủy Diệt");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.clan != null) {
                                    if (player.clan.khiGas != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_GAS,
                                                "Bang hội của ngươi đang đi Khí Gas cấp độ "
                                                + player.clan.khiGas.level + "\nNgươi có muốn đi bú không?",
                                                "Đồng ý", "Từ chối");
                                    } else {

                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_GAS,
                                                "Đây là khí Gas chết chóc \nCác ngươi cứ yên tâm lên đường\n"
                                                + "Ở đây có ta lo cho Người yêu ngươi rồi\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    ChangeMapService.gI().goToGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của ngươi phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    Input.gI().createFormChooseLevelGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của ngươi phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCPET_GO_TO_GAS) {
                        switch (select) {
                            case 0:
                                GasService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        };
    }

    //    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 48) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn gì nào?" + player.inventory.coupon+, "Đóng");
//                    } else {
//                        super.openBaseMenu(player);
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    switch (this.mapId) {
//                        case 48:
//                            switch (player.iDMark.getIndexMenu()) {
//                                case ConstNpc.BASE_MENU:
//                                    if (select == 0) {
//
//                                    }
//                                    break;
//                            }
//                            break;
//                    }
//                }
//            }
//        };
//    }
    //public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
    //    return new Npc(mapId, status, cx, cy, tempId, avartar) {
    //        @Override
    //        public void openBaseMenu(Player player) {
    //            if (canOpenNpc(player)) {
    //                createOtherMenu(player, ConstNpc.BASE_MENU,
    //                        "Hãy thu thập đủ x99 thức ăn , ta sẽ ban cho ngươi trang bị huỷ diệt!",
    //                        "Xem Điểm ", "Shop Pudding", "Shop Xúc Xích", "Shop Kem Dâu", "Shop Mì Ly", "Shop Sushi");
    //            }
    ///        }
//
    //        @Override
    //        public void confirmMenu(Player player, int select) {
    //            if (canOpenNpc(player)) {
    //                switch (this.mapId) {
    //                    case 48:
    //                        switch (player.iDMark.getIndexMenu()) {
    //                            case ConstNpc.BASE_MENU:
    //                                if (select == 0) {
    //                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi đang có: " + player.inventory.coupon + " điểm", "Đóng");
    //                                    break;
     //                               }
    //                                
     //                       }
     //                       break;
      //              }
      //          }
      //      }
       // };
    //}

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, cậu muốn tôi giúp gì?", "Nhiệm vụ\nhàng ngày", "Từ chối");
                    }
//                    if (this.mapId == 47) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Xin chào, cậu muốn tôi giúp gì?", "Từ chối");
//                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 46) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hê Hê Hê ta là thần mèo Karin?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 46) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
//                                ShopServiceNew.gI().opendShop(player, "KARIN", true);
                                Service.gI().sendThongBao(player, "Bấm cái nịt, sign : JAKE!");
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Ta Vừa Hắc Mắp Xêm Được Tóp Của Toàn Server\b|7|Người Muốn Xem Tóp Gì?",
                            "Tóp Sức Mạnh", "Top Nhiệm Vụ", "Top Pvp", "Tóp Ngũ Hành Sơn", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 5:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        Service.gI().showListTop(player, Manager.topSM);
                                        break;
                                    }
                                    if (select == 1) {
                                        Service.gI().showListTop(player, Manager.topNV);
                                        break;
                                    }
                                    if (select == 2) {
                                        Service.gI().showListTop(player, Manager.topPVP);
                                        break;
                                    }
                                    if (select == 3) {
//                                        Service.gI().showListTop(player, Manager.topNHS);
                                        break;
                                    }
                                    break;
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                    } else if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Cua hang", "Doi da ngu sac", "doi de", "Đỏi thỏi vàng", "Doi Hong Ngoc", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 131) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                }
                            }
                            if (this.mapId == 5) {
                                if (select == 0) {
                                    ShopServiceNew.gI().opendShop(player, "GOKUSSJ", true);
                                    break;
                                } else if (select == 1) {
                                    Input.gI().createMenuDoiDaNguSac(player);
                                    break;
                                } else if (select == 2) {
                                    this.createOtherMenu(player, 1, "Con dang co: " + GodGK.GetMoney((player.getSession().userId)) + "\nCon muon doi de tu xin hay khong?\n|0|Tuy chon1 ) De Nezuko [50k]: Tang 30% pt khi hop the\n|0|Tuy chon2 ) De Maicute [100k]: Tang 100% pt khi hop the\n|0|Tuy chon3 ) De Zamatsu [200k]: Tang 150% pt khi hop the", "Tuy chon\n1", "Tuy chon 2", "Tuy chon 3", "Huy");
                                } else if (select == 3) {
                                    Input.gI().createMenuDoiThoiVang(player);
                                } else if (select == 4) {
                                    Input.gI().createMenuDoiHongNgoc(player);
                                    break;
                                }
                            }
                            break;
                        case 1:
                            switch (select) {

                                case 0:
                                    if (GodGK.GetMoney((player.getSession().userId)) >= 50000) {
                                        PetService.gI().changeNezukoPet(player);
                                        GodGK.setMoney(player, GodGK.GetMoney(player.getSession().userId) - 50000);
                                    } else {
                                        Service.gI().sendThongBao(player, "Ban con thieu " + (50000 - GodGK.GetMoney((player.getSession().userId))) + " nua");
                                    }
                                    break;

                                case 1:
                                    if (GodGK.GetMoney((player.getSession().userId)) >= 100000) {
                                        PetService.gI().createMaicutePet(player, player.gender);
                                        GodGK.setMoney(player, GodGK.GetMoney(player.getSession().userId) - 100000);
                                    } else {
                                        Service.gI().sendThongBao(player, "Ban con thieu " + (100000 - GodGK.GetMoney((player.getSession().userId))) + " nua");
                                    }
                                    break;

                                
                                case 2:
                                    if (GodGK.GetMoney((player.getSession().userId)) >= 200000) {
                                        PetService.gI().createZamatsuPet(player, player.gender);
                                        GodGK.setMoney(player, GodGK.GetMoney(player.getSession().userId) - 200000);
                                    } else {
                                        Service.gI().sendThongBao(player, "Ban con thieu " + (200000 - GodGK.GetMoney((player.getSession().userId))) + " nua");
                                    }
                                    break;

                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + biKiep.quantity + " bí kiếp.\n"
                                    + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart", "Học dịch\nchuyển", "Đóng");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                InventoryServiceNew.gI().addItemBag(player, yardart);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                            }
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        };
    }

    public static Npc npc70(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngài Muốn Làm Gì?",
                            "Quay Về", "Từ chối");

                
                
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 174) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, -1);
                                    break;
                               

                            }
                        }
                    }
                    if (this.mapId == 144) {
                        switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 354);
                                break;
                        }
                    }
                }
            }
        };
    }
    public static Npc VEGETA(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 ) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "SHOP VIP", false);
                                    break;
                                
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                
                
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MR_POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.GIUMA_DAU_BO:
                    return Giuma(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NOI_BANH:
                    return noibanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.PIC2MAI:
                    return Pic2Mai(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.ONG_BUT:
                    return ongbut(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC70:
                    return npc70(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VEGETA:
                    return VEGETA(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.BULMAA:
                    return bulmapt(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.JIREN:
                    return jiren(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THIENSU:
                    return thiensu(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.JACO:
//                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.POTAGE:
//                    return Potage(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                //case ConstNpc.LY_TIEU_NUONG:
                    //return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HUNGVUONG:
                    return hungvuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
                //case ConstNpc.BILL:
                //    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                            }
                        }
                    };
            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class, e, "Lỗi load npc");
            return null;
        }
    }

    //girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }
    public static void createNpcNRHUYDIET() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }
    
    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0, "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.", "Hướng\nDẫn\nThêm", "Mua Bán\nKý Gửi", "Từ Chối");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            //Service.getInstance().sendPopUpMultiLine(pl, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 5 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:                          
                            if (pl.getSession().actived) {
                            
                                ShopKyGuiService.gI().openShopKyGui(pl);                               
                            }else{
                                //Service.gI().sendThongBaoFromAdmin(pl,"|5|VUI LÒNG Kích Hoạt Thành Viên\n|!");
                            }     
                            break;
                    }
                }
            }
        };
    }    

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP: //                        if (player.getSession().actived) 
                    {
                        if (Maintenance.isRuning) {
                            break;
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                        break;
                    }
//                        else {
//                            Service.getInstance().sendThongBao(player, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN TẠI\n|7|NROGOD.COM\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG");
//                            break;
//                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                        ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2085:
                    case ConstNpc.MENU_OPTION_USE_ITEM2086:
                    case ConstNpc.MENU_OPTION_USE_ITEM2087:
                        try {
                        ItemService.gI().openDoHuyDiet(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.getInstance().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.getInstance().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.ACTIVE_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().ActivePlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.getInstance().sendThongBao(player, "Activated  " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                }
                                InventoryServiceNew.gI().sendItemBags(player);
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    if (player.pet.typePet == 1) {
//                                        PetService.gI().changePicPet(player);
                                    } else if (player.pet.typePet == 2) {
                                        PetService.gI().changeMabuPet(player);
                                    }
                                    PetService.gI().changeBerusPet(player);
                                }
                                break;
                            case 2:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
//                                PlayerService.gI().baoTri();
                                    Maintenance.gI().start(15);
                                    System.out.println(player.name);
                                }
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                        "Chọn Boss?", "Full Cụm\nANDROID", "BLACK", "BROLY", "Cụm\nCell",
                                        "Cụm\nDoanh trại", "DOREMON", "FIDE", "FIDE\nBlack", "Cụm\nGINYU", "Cụm\nNAPPA", "NGỤC\nTÙ");
                                break;
                            case 5:
                                Input.gI().createFormSenditem(player);
                                break;
                            case 6:
                                Input.gI().createFormSenditem1(player);
                                break;
                            case 7:
                                Input.gI().createFormSenditem2(player);
                                break;
                            case 8:
                                Input.gI().createFormSendGoldBar(player);
                                break;
                        }
                        break;

                    case ConstNpc.CALL_BOSS:
                        switch (select) {
                            case 0:
                                BossManager.gI().createBoss(BossID.ANDROID_13);
                                BossManager.gI().createBoss(BossID.ANDROID_14);
                                BossManager.gI().createBoss(BossID.ANDROID_15);
                                BossManager.gI().createBoss(BossID.ANDROID_19);
                                BossManager.gI().createBoss(BossID.DR_KORE);
                                BossManager.gI().createBoss(BossID.KING_KONG);
                                BossManager.gI().createBoss(BossID.PIC);
                                BossManager.gI().createBoss(BossID.POC);
                                break;
                            case 1:
                                BossManager.gI().createBoss(BossID.BLACK);
                                break;
                            case 2:
                                BossManager.gI().createBoss(BossID.BROLY);
                                break;
                            case 3:
                                BossManager.gI().createBoss(BossID.SIEU_BO_HUNG);
                                BossManager.gI().createBoss(BossID.XEN_BO_HUNG);
                                break;
                            case 4:
                                Service.getInstance().sendThongBao(player, "Không có boss");
                                break;
                            case 5:
                                BossManager.gI().createBoss(BossID.CHAIEN);
                                BossManager.gI().createBoss(BossID.XEKO);
                                BossManager.gI().createBoss(BossID.XUKA);
                                BossManager.gI().createBoss(BossID.NOBITA);
                                BossManager.gI().createBoss(BossID.DORAEMON);
                                break;
                            case 6:
                                BossManager.gI().createBoss(BossID.FIDE);
                                break;
                            case 7:
                                BossManager.gI().createBoss(BossID.FIDE_ROBOT);
                                BossManager.gI().createBoss(BossID.VUA_COLD);
                                break;
                            case 8:
                                BossManager.gI().createBoss(BossID.SO_1);
                                BossManager.gI().createBoss(BossID.SO_2);
                                BossManager.gI().createBoss(BossID.SO_3);
                                BossManager.gI().createBoss(BossID.SO_4);
                                BossManager.gI().createBoss(BossID.TIEU_DOI_TRUONG);
                                break;
                            case 9:
                                BossManager.gI().createBoss(BossID.KUKU);
                                BossManager.gI().createBoss(BossID.MAP_DAU_DINH);
                                BossManager.gI().createBoss(BossID.RAMBO);
                                break;
                            case 10:
                                BossManager.gI().createBoss(BossID.COOLER_GOLD);
                                BossManager.gI().createBoss(BossID.CUMBER);
                                BossManager.gI().createBoss(BossID.SONGOKU_TA_AC);
                                break;
                        }
                        break;

                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyoken(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejoko(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddam(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummon(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalick(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkey(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhp(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.getInstance().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_ACTIVE:
                        switch (select) {
                            case 0:
                                if (player.getSession().goldBar >= 20) {
                                    player.getSession().actived = true;
                                    if (PlayerDAO.subGoldBar(player, 20)) {
                                        Service.getInstance().sendThongBao(player, "Đã mở thành viên thành công!");
                                        break;
                                    } else {
                                        this.npcChat(player, "Lỗi vui lòng báo admin...");
                                    }
                                }
//                                Service.getInstance().sendThongBao(player, "Bạn không có vàng\n Vui lòng NROGOD.COM để nạp thỏi vàng");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.getInstance().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_ADMIN_PHU:
                        switch (select) {
                            case 0:
                                Input.gI().createFormFindPlayer1(player);
                                break;
                            case 1:
                                if (player.isAdmin1()) {
                                    System.out.println(player.name);
//                                PlayerService.gI().baoTri();
                                    Maintenance.gI().start(15);
                                    System.out.println(player.name);
                                }
                                break;
                            case 2:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                        "Chọn Boss?", "Full Cụm\nANDROID", "BLACK", "BROLY", "Cụm\nCell",
                                        "Cụm\nDoanh trại", "DOREMON", "FIDE", "FIDE\nBlack", "Cụm\nGINYU", "Cụm\nNAPPA", "NGỤC\nTÙ");

                                break;
                            case 3:
                                BossManager.gI().showListBoss(player);
                                break;
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER_PHU: {
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 1:
                                    String[] selectss = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.ACTIVE_PLAYER, -1,
                                            "Bạn có chắc chắn muốn mở thành viên cho " + p.name, selectss, p);
                                    break;
                            }
                        }
                    }

                    break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.getInstance().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                                case 5:
                                    String[] selectss = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.ACTIVE_PLAYER, -1,
                                            "Bạn có chắc chắn muốn mở thành viên cho " + p.name, selectss, p);
                                    break;
                                case 6:
                                    Input.gI().createFormSendGoldBar(player);
                                    break;

                            }
                        }
                        break;
//                    case ConstNpc.MENU_EVENT:
//                        switch (select) {
//                            case 0:
//                                Service.getInstance().sendThongBaoOK(player, "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
//                                break;
//                            case 1:
//                                Util.showListTop(player, (byte) 2);
//                                break;
//                            case 2:
//                                Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc...");
////                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
////                                        "100 bông", "1000 bông", "10000 bông");
//                                break;
//                            case 3:
//                                Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc...");
////                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện đấy... ",
////                                        "Đồng ý", "Từ chối");
//                                break;
//
//                        }
//                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.getInstance().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
