package cn.hm.data;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import junit.framework.TestCase;

public class AuthenticationTest extends TestCase{
	// �û���½���˳�
    public void testLoginAndLogout() {

        // ����securityManager������ͨ��ini�����ļ�����securityManager����
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-first.ini");

        // ����SecurityManager
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();

        // ��securityManager���õ�ǰ�����л�����
        SecurityUtils.setSecurityManager(securityManager);

        // ��SecurityUtils��ߴ���һ��subject
        Subject subject = SecurityUtils.getSubject();

        // ����֤�ύǰ׼��token�����ƣ�
        // ������˺ź����� ���������û������ȥ
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan",
                "111111");

        try {
            // ִ����֤�ύ
            subject.login(token);
        } catch (AuthenticationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // �Ƿ���֤ͨ��
        boolean isAuthenticated = subject.isAuthenticated();

        System.out.println("�Ƿ���֤ͨ����" + isAuthenticated);

        // �˳�����
        subject.logout();

        // �Ƿ���֤ͨ��
        isAuthenticated = subject.isAuthenticated();

        System.out.println("�Ƿ���֤ͨ����" + isAuthenticated);

    }

    // �Զ���realm
    public void testCustomRealm() {

        // ����securityManager������ͨ��ini�����ļ�����securityManager����
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");

        // ����SecurityManager
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();

        // ��securityManager���õ�ǰ�����л�����
        SecurityUtils.setSecurityManager(securityManager);

        // ��SecurityUtils��ߴ���һ��subject
        Subject subject = SecurityUtils.getSubject();

        // ����֤�ύǰ׼��token�����ƣ�
        // ������˺ź����� ���������û������ȥ
        UsernamePasswordToken token = new UsernamePasswordToken("111",
                "222222");

        try {
            // ִ����֤�ύ
            subject.login(token);
        } catch (AuthenticationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // �Ƿ���֤ͨ��
        boolean isAuthenticated = subject.isAuthenticated();

        System.out.println("�Ƿ���֤ͨ����" + isAuthenticated);

    }
}
