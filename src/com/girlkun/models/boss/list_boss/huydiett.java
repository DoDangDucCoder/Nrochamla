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
public class huydiett extends Boss {

    public huydiett() throws Exception {
        super(BossID.HUYDIET, BossesData.HUYDIET1);
    }

    @Override
    public void reward(Player plKill) {
        if (plKill != null) {
            if (Util.isTrue(50, 100)) {
                if (Util.isTrue(30, 100)) {
                    ItemMap it = new ItemMap(this.zone, 657, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
                    it.options.add(new Item.ItemOption(0,  Util.nextInt(6000, 12000)));
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
                    ItemMap it = new ItemMap(this.zone, 659, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plKill.id);
                    it.options.add(new Item.ItemOption(0, Util.nextInt(6000, 12000)));
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

}
