const CLASS_BTN_BALANCE_PURCHASE = "btn_balance_purchase";
const CLASS_INSUFFICIENT_FUNDS = "insufficient_funds";
let btn = document.getElementsByClassName(CLASS_BTN_BALANCE_PURCHASE)[0];
let msg = document.getElementsByClassName(CLASS_INSUFFICIENT_FUNDS)[0];

if (balance < price){
    btn.style.display = 'none';
    msg.style.display = 'block';
}