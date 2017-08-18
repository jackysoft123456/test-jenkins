package cn.hm.data;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class CustomRealm extends AuthorizingRealm {
	// ����realm������
    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    // ������֤
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
    	UsernamePasswordToken tk = (UsernamePasswordToken)token;
        // token���û������
        // ��һ����token��ȡ�������Ϣ
        String userCode = (String) tk.getPrincipal();

        // �ڶ����������û������userCode�����ݿ��ѯ
        // ....

        // �����ѯ��������null
        // ���ݿ����û��˺���zhangsansan
        /*
         * if(!userCode.equals("zhangsansan")){// return null; }
         */

        // ģ������ݿ��ѯ������
        String password = new String(tk.getPassword());

        // �����ѯ��������֤��ϢAuthenticationInfo

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userCode, password, this.getName());

        return simpleAuthenticationInfo;
    }

    // ������Ȩ
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        // TODO Auto-generated method stub
        return null;
    }

}
