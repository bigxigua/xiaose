<#--

    Symphony - A modern community (forum/BBS/SNS/blog) platform written in Java.
    Copyright (C) 2012-present, b3log.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

-->
<#include "../macro-head.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${registerLabel} - ${symphonyLabel}">
        <meta name="description" content="${registerLabel} ${symphonyLabel}"/>
        </@head>
        <link rel="stylesheet" href="${staticServePath}/css/index.css?${staticResourceVersion}" />
        <link rel="canonical" href="${servePath}/register">
        <script src="//captcha.luosimao.com/static/dist/api.js"></script>
    </head>
    <body>
        <#include "../header.ftl">
        <div class="main">
            <div class="wrapper verify">
                <div class="verify-wrap">
                    <div class="form">
                        <svg><use xlink:href="#logo"></use></svg>

                        <div class="input-wrap">
                            <svg><use xlink:href="#userrole"></use></svg>
                            <input id="registerUserName" type="text" placeholder="${userNamePlaceholderLabel}" autocomplete="off" autofocus="autofocus" />
                        </div>
                        <div class="input-wrap">
                            <svg><use xlink:href="#email"></use></svg>
                            <input id="registerUserEmail" type="text" placeholder="${mobilePlaceholderLabel}" autocomplete="off" />
                        </div>
<#--                        <div class="input-wrap<#if "2" == miscAllowRegister> fn-none</#if>">-->
<#--                            <img id="registerCaptchaImg" class="fn-pointer captcha-img " src="${servePath}/captcha" onclick="this.src = '${servePath}/captcha?' + (new Date()).getTime()" />-->
<#--                            <input type="text" id="registerCaptcha" class="captcha-input" placeholder="${captchaLabel}" />-->
<#--                        </div>-->
                        <div class="l-captcha" data-callback="getResponse" data-site-key="0e72251b23e593f6d364edd438cc2880"></div>
                        <div class="input-wrap">
                            <svg><use xlink:href="#userrole"></use></svg>
                            <input id="phoneVerifyCode" type="text" placeholder="${phoneVerifyCodePlaceholderLabel}" autocomplete="off" />
                        </div>
                        <div class="input-wrap<#if "2" != miscAllowRegister> fn-none</#if>">
                            <svg><use xlink:href="#heart"></use></svg>
                            <input id="registerInviteCode" type="text" placeholder="${invitecodePlaceholderLabel}" autocomplete="off" />
                        </div>
                        <div class="input-wrap">
                            <svg><use xlink:href="#locked"></use></svg>
                            <input type="password" autofocus="autofocus" id="registerUserPassword2" placeholder="${passwordLabel}" />
                        </div>
                        <div class="input-wrap">
                            <svg><use xlink:href="#locked"></use></svg>
                            <input type="password" id="registerConfirmPassword2" placeholder="${userPasswordLabel2}" />
                        </div>
                        <div id="registerTip" class="tip"></div>
                        <input id="referral" type="hidden" value="${referral}">
                        <button class="green" id="registerBtn" onclick="Verify.register()">${registerLabel}</button>
                        <button onclick="Util.goLogin()">${loginLabel}</button>
                    </div>
                </div>
                <div class="intro vditor-reset">
                    ${introLabel}
                </div>
            </div>
        </div>
        <#include "../footer.ftl">
        <script src="${staticServePath}/js/verify${miniPostfix}.js?${staticResourceVersion}"></script>
        <script>
            if ('${referral}' !== '') {
                sessionStorage.setItem('r', '${referral}');
            }
            Verify.init();
            Label.invalidUserNameLabel = "${invalidUserNameLabel}";
            Label.invalidEmailLabel = "${invalidPhoneLabel}";
            Label.confirmPwdErrorLabel = "${confirmPwdErrorLabel}";
            Label.captchaErrorLabel = "${captchaErrorLabel}";
            Label.invalidPhoneVerifyCodeLabel = "${invalidPhoneVerifyCodeLabel}";


            function getResponse(resp){
                console.log(resp);  // resp 即验证成功后获取的值

                var requestJSONObject = {
                    phoneNo: $("#registerUserEmail").val().replace(/(^\s*)|(\s*$)/g, ""),
                    lsmRespCode: resp
                };
                $.ajax({
                    url: Label.servePath + "/sendPhoneCode",
                    type: "POST",
                    cache: false,
                    data: JSON.stringify(requestJSONObject),
                    success: function (result, textStatus) {
                        if (result.sc) {
                            $("#registerTip").addClass('succ').html('<ul><li>' + result.msg + '</li></ul>');
                        } else {
                            $("#registerTip").addClass('error').removeClass('succ').html('<ul><li>' + result.msg + '</li></ul>');
                            LUOCAPTCHA.reset()
                        }
                    }
                });
            }
        </script>
    </body>
</html>
