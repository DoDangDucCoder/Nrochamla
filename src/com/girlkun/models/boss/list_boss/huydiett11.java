/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.models.boss.list_boss;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 *
 * @author Administrator
 */
public class huydiett11 extends Boss {

    public huydiett11() throws Exception {
        super(BossID.HUYDIETTT, BossesData.HUYDIET3, BossesData.HUYDIET4);
    }

    @Override
    public void reward(Player plKill) {
        if (plKill != null) {
            if (Util.isTrue(65, 100)) {
                if (Util.isTrue(65, 100)) {
                    ItemMap it = new ItemMap(this.zone, 2006, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
//                    it.options.add(new Item.ItemOption(1,  Util.nextInt(5, 30)));
                    it.options.add(new Item.ItemOption(30, 0));
                    it.options.add(new Item.ItemOption(87, 0));
                    it.options.add(new Item.ItemOption(93, 1));
//                    it.options.add(new Item.ItemOption(14, 10));
//                    it.options.add(new Item.ItemOption(160, 30));
//                    it.options.add(new Item.ItemOption(159, 4));
//                    if (Util.isTrue(80, 100)) {
//                        it.options.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
//                    }
                    Service.getInstance().dropItemMap(this.zone, it);
                }
                 if (Util.isTrue(40, 100)) {
                    ItemMap it = new ItemMap(this.zone, 2085, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
//                    it.options.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
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

                if (Util.isTrue(60, 100)) {
                    ItemMap it = new ItemMap(this.zone, 2086, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
//                    it.options.add(new Item.ItemOption(93,  Util.nextInt(1, 3)));
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
                if (Util.isTrue(60, 100)) {
                    ItemMap it = new ItemMap(this.zone, 2087, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
//                    it.options.add(new Item.ItemOption(93,  Util.nextInt(1, 3)));
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

}
