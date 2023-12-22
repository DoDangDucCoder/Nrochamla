package com.girlkun.models.boss.list_boss;

import com.girlkun.models.boss.list_boss.fide.*;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;


public class robot extends Boss {

    public robot() throws Exception {
        super(BossID.ROBOT, BossesData.ROBOT);
    }
  
    @Override
    public void reward(Player plKill) {
        if (plKill != null) {
            if (Util.isTrue(50, 100)) {
                if (Util.isTrue(30, 100)) {
                    ItemMap it = new ItemMap(this.zone, 1054, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
                    it.options.add(new Item.ItemOption(0,  Util.nextInt(10000, 20000)));
                    it.options.add(new Item.ItemOption(30, 0));
                    it.options.add(new Item.ItemOption(87, 0));
//                    it.options.add(new Item.ItemOption(14, 10));
//                    it.options.add(new Item.ItemOption(160, 30));
//                    it.options.add(new Item.ItemOption(159, 4));
//                    if (Util.isTrue(80, 100)) {
//                        it.options.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
//                    }
                    Service.getInstance().dropItemMap(this.zone, it);
                }
                if (Util.isTrue(30, 100)) {
                    ItemMap it = new ItemMap(this.zone, 1055, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
                    it.options.add(new Item.ItemOption(0,  Util.nextInt(10000, 20000)));
                    it.options.add(new Item.ItemOption(30, 0));
                    it.options.add(new Item.ItemOption(87, 0));
//                    it.options.add(new Item.ItemOption(14, 10));
//                    it.options.add(new Item.ItemOption(160, 30));
//                    it.options.add(new Item.ItemOption(159, 4));
//                    if (Util.isTrue(80, 100)) {
//                        it.options.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
//                    }
                    Service.getInstance().dropItemMap(this.zone, it);
                }
                if (Util.isTrue(30, 100)) {
                    ItemMap it = new ItemMap(this.zone, 1056, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
                    it.options.add(new Item.ItemOption(0,  Util.nextInt(10000, 20000)));
                    it.options.add(new Item.ItemOption(30, 0));
                    it.options.add(new Item.ItemOption(87, 0));
//                    it.options.add(new Item.ItemOption(14, 10));
//                    it.options.add(new Item.ItemOption(160, 30));
//                    it.options.add(new Item.ItemOption(159, 4));
//                    if (Util.isTrue(80, 100)) {
//                        it.options.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
//                    }
                    Service.getInstance().dropItemMap(this.zone, it);
                }
//                if (Util.isTrue(30, 100)) {
//                    ItemMap it = new ItemMap(this.zone, 1241, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                            this.location.y - 24), plKill.id);
////                    it.options.add(new Item.ItemOption(0,  Util.nextInt(6000, 12000)));
//                    it.options.add(new Item.ItemOption(30, 0));
//                    it.options.add(new Item.ItemOption(87, 0));
////                    it.options.add(new Item.ItemOption(14, 10));
////                    it.options.add(new Item.ItemOption(160, 30));
////                    it.options.add(new Item.ItemOption(159, 4));
////                    if (Util.isTrue(80, 100)) {
////                        it.options.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
////                    }
//                    Service.getInstance().dropItemMap(this.zone, it);
//                }
                 if (Util.isTrue(30, 100)) {
                    ItemMap it = new ItemMap(this.zone, Util.nextInt(1230, 1240), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                        this.location.y - 24), plKill.id);
                    it.options.add(new Item.ItemOption(50, Util.nextInt(5, 30)));
                    it.options.add(new Item.ItemOption(77,  Util.nextInt(5, 30)));
                    it.options.add(new Item.ItemOption(103,  Util.nextInt(5, 30)));
//                    it.options.add(new Item.ItemOption(14, 10));
                    it.options.add(new Item.ItemOption(5,  Util.nextInt(5, 20)));
                    it.options.add(new Item.ItemOption(30, 0));
                    if (Util.isTrue(98, 100)) {
                        it.options.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
                    }
                    Service.getInstance().dropItemMap(this.zone, it);
                }

                if (Util.isTrue(30, 100)) {
                    ItemMap it = new ItemMap(this.zone, 661, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
                    it.options.add(new Item.ItemOption(0,  Util.nextInt(6000, 12000)));
                    it.options.add(new Item.ItemOption(30, 0));
                    it.options.add(new Item.ItemOption(87, 0));
//                    it.options.add(new Item.ItemOption(14, 10));
//                    it.options.add(new Item.ItemOption(101, 50));
//                    it.options.add(new Item.ItemOption(159, 4));
//                    if (Util.isTrue(80, 100)) {
//                        it.options.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
//                    }
                    Service.getInstance().dropItemMap(this.zone, it);
                }
            } else {
                ItemMap it = new ItemMap(this.zone, Util.nextInt(16, 20), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                        this.location.y - 24), plKill.id);
                Service.getInstance().dropItemMap(this.zone, it);
            }
        }
    }
  @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 3600000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;
 
     @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
//    @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//
//    @Override
//    public void reward(Player plKill) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.reward(plKill);
//    }
//
//    @Override
//    protected void notifyJoinMap() {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.notifyJoinMap();
//    }
}





















