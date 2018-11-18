/**
 * 检验注册信息合法性
 */
function checkRegisterInfo (username,password,repassword,captcha,agree) {
    var regx = /^[A-Za-z0-9]*$/;
    var regmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
    
    if(username == "" || !regmail.test(username)) //检验邮箱格式 
    {
        window.alert("邮箱不合法!");
        return false;  
    } 
    if(password == "" || !regx.test(password)) //检验密码
    {
        window.alert("密码不合法!");
        return false;  
    } 
    if(repassword == "" || !regx.test(repassword)) //检验确认密码  
    {
        window.alert("用户名不合法!");
        return false;  
    }
    if(password != repassword)
    {
        window.alert("两次输入的密码不一致!");
        return false;  
    } 

    // if(agree != "on")
    // {
        // window.alert("不可反对条款规定!");
        // return false;         
    // }
    return true;    
}