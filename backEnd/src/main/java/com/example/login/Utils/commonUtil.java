package com.example.login.Utils;

public class commonUtil {
    public int getInt(String s){
        if (s == "电气工程与自动化学院") return 0;
        else if(s == "机械工程及自动化学院")return 1;
        else if(s == "数学与计算机科学学院" || s == "软件学院" ) return 2;
        else if(s == "石油化工学院" || s == "化学学院" || s == "材料科学与工程学院" || s == "环境与资源学院") return 3;
        else if (s == "土木工程学院" || s == "建筑学院") return 4;
        else if(s == "生物科学与工程学院") return 5;
        else if (s == "经济与管理学院") return 6;
        else if (s == "外国语学院") return 7;
        else if (s == "物理与信息工程学院") return 8;
        else if (s == "紫金矿业学院") return 9;
        else if(s == "法学院") return 10;
        else return 11;
    }
//    public String getString(int i){
//        if (i == 0) return "电气工程与自动化学院";
//        else if(i == "机械工程及自动化学院")return 1;
//        else if(i == "数学与计算机科学学院" || s == "软件学院" ) return 2;
//        else if(i == "石油化工学院" || s == "化学学院" || s == "材料科学与工程学院" || s == "环境与资源学院") return 3;
//        else if (i == "土木工程学院" || s == "建筑学院") return 4;
//        else if(i == 5) return "生物科学与工程学院";
//        else if (i == 6) return "经济与管理学院";
//        else if (i == 7) return "外国语学院";
//        else if (i == 8) return "物理与信息工程学院";
//        else if (i == 9) return "紫金矿业学院";
//        else if(i == 10) return "法学院";
//        else return 11;
//    }
}
