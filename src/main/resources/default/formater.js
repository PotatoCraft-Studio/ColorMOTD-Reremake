/*
 * ColorMOTD
 * Default TPSFormater
 * by andylizi
 */

/* global tps */

if(tps > 18){
    '良好';
}else if(tps > 15){
    '中等';
}else if(tps > 10){
    '差';
}else{
    '极差';
}