//package com.example.plusnote.entities;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//import java.io.Serializable;
//
//@Entity(tableName = "listnotes")
//public class ListNote implements Serializable {
//
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//
//    @ColumnInfo(name = "title_list")
//    private String title;
//
//    @ColumnInfo(name = "item1")
//    private String item1;
//
//    @ColumnInfo(name = "item2")
//    private String item2;
//
//    @ColumnInfo(name = "item3")
//    private String item3;
//
//    @ColumnInfo(name = "item4")
//    private String item4;
//
//    @ColumnInfo(name = "item5")
//    private String item5;
//
//    @ColumnInfo(name = "item6")
//    private String item6;
//
//    @ColumnInfo(name = "item7")
//    private String item7;
//
//    @ColumnInfo(name = "item8")
//    private String item8;
//
//    @ColumnInfo(name = "item9")
//    private String item9;
//
//    @ColumnInfo(name = "item10")
//    private String item10;
//
//    @ColumnInfo(name = "item11")
//    private String item11;
//
//    @ColumnInfo(name = "item12")
//    private String item12;
//
//    @ColumnInfo(name = "item13")
//    private String item13;
//
//    @ColumnInfo(name = "item14")
//    private String item14;
//
//    @ColumnInfo(name = "item15")
//    private String item15;
//
//    @ColumnInfo(name = "item16")
//    private String item16;
//
//    @ColumnInfo(name = "item17")
//    private String item17;
//
//    @ColumnInfo(name = "item18")
//    private String item18;
//
//    @ColumnInfo(name = "item19")
//    private String item19;
//
//    @ColumnInfo(name = "item20")
//    private String item20;
//
//    @ColumnInfo(name = "item21")
//    private String item21;
//
//    @ColumnInfo(name = "item22")
//    private String item22;
//
//    @ColumnInfo(name = "item23")
//    private String item23;
//
//    @ColumnInfo(name = "item24")
//    private String item24;
//
//
//
//
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String[] getItems() {
//        String[] items = new String[0];
//        return items;
//    }
//
//    public void setItems(String[] items) {
//        item1 = items[0];
//        item2 = items[0];
//        item3 = items[0];
//        item4 = items[0];
//        item5 = items[0];
//        item6 = items[0];
//        item7 = items[0];
//        item8 = items[0];
//        item9 = items[0];
//        item10 = items[0];
//        item11 = items[0];
//        item12 = items[0];
//        item13 = items[0];
//        item14 = items[0];
//        item15 = items[0];
//        item16 = items[0];
//        item17 = items[0];
//        item18 = items[0];
//        item19 = items[0];
//        item20 = items[0];
//        item21 = items[0];
//        item22 = items[0];
//        item23 = items[0];
//        item24 = items[0];
//    }
//
//    public String getItem1() {
//        return item1;
//    }
//
//    public void setItem1(String item1) {
//        this.item1 = item1;
//    }
//
//    public String getItem2() {
//        return item2;
//    }
//
//    public void setItem2(String item2) {
//        this.item2 = item2;
//    }
//
//    public String getItem3() {
//        return item3;
//    }
//
//    public void setItem3(String item3) {
//        this.item3 = item3;
//    }
//
//    public String getItem4() {
//        return item4;
//    }
//
//    public void setItem4(String item4) {
//        this.item4 = item4;
//    }
//
//    public String getItem5() {
//        return item5;
//    }
//
//    public void setItem5(String item5) {
//        this.item5 = item5;
//    }
//
//    public String getItem6() {
//        return item6;
//    }
//
//    public void setItem6(String item6) {
//        this.item6 = item6;
//    }
//
//    public String getItem7() {
//        return item7;
//    }
//
//    public void setItem7(String item7) {
//        this.item7 = item7;
//    }
//
//    public String getItem8() {
//        return item8;
//    }
//
//    public void setItem8(String item8) {
//        this.item8 = item8;
//    }
//
//    public String getItem9() {
//        return item9;
//    }
//
//    public void setItem9(String item9) {
//        this.item9 = item9;
//    }
//
//    public String getItem10() {
//        return item10;
//    }
//
//    public void setItem10(String item10) {
//        this.item10 = item10;
//    }
//
//    public String getItem11() {
//        return item11;
//    }
//
//    public void setItem11(String item11) {
//        this.item11 = item11;
//    }
//
//    public String getItem12() {
//        return item12;
//    }
//
//    public void setItem12(String item12) {
//        this.item12 = item12;
//    }
//
//    public String getItem13() {
//        return item13;
//    }
//
//    public void setItem13(String item13) {
//        this.item13 = item13;
//    }
//
//    public String getItem14() {
//        return item14;
//    }
//
//    public void setItem14(String item14) {
//        this.item14 = item14;
//    }
//
//    public String getItem15() {
//        return item15;
//    }
//
//    public void setItem15(String item15) {
//        this.item15 = item15;
//    }
//
//    public String getItem16() {
//        return item16;
//    }
//
//    public void setItem16(String item16) {
//        this.item16 = item16;
//    }
//
//    public String getItem17() {
//        return item17;
//    }
//
//    public void setItem17(String item17) {
//        this.item17 = item17;
//    }
//
//    public String getItem18() {
//        return item18;
//    }
//
//    public void setItem18(String item18) {
//        this.item18 = item18;
//    }
//
//    public String getItem19() {
//        return item19;
//    }
//
//    public void setItem19(String item19) {
//        this.item19 = item19;
//    }
//
//    public String getItem20() {
//        return item20;
//    }
//
//    public void setItem20(String item20) {
//        this.item20 = item20;
//    }
//
//    public String getItem21() {
//        return item21;
//    }
//
//    public void setItem21(String item21) {
//        this.item21 = item21;
//    }
//
//    public String getItem22() {
//        return item22;
//    }
//
//    public void setItem22(String item22) {
//        this.item22 = item22;
//    }
//
//    public String getItem23() {
//        return item23;
//    }
//
//    public void setItem23(String item23) {
//        this.item23 = item23;
//    }
//
//    public String getItem24() {
//        return item24;
//    }
//
//    public void setItem24(String item24) {
//        this.item24 = item24;
//    }
//
//}
