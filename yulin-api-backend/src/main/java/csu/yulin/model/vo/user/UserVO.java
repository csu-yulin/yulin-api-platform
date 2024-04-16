package csu.yulin.model.vo.user;

import csu.yulin.model.entity.User;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username; // 脱敏后的用户名
    private String email; // 脱敏后的电子邮箱
    private String phone; // 脱敏后的电话
    private String gender;
    private String avatar;
    private String role;
    private Integer status;
    private LocalDateTime createdTime;


    // 构造方法或静态方法用于转换User对象为UserVO对象
    public static UserVO fromUser(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        // 对电子邮箱进行脱敏处理，例如只显示@符号前的部分加上星号
        userVO.setEmail(maskEmail(user.getEmail()));
        // 对电话进行脱敏处理
        userVO.setPhone(maskPhone(user.getPhone()));
        userVO.setGender(user.getGender());
        userVO.setAvatar(user.getAvatar());
        userVO.setRole(user.getRole());
        userVO.setStatus(user.getStatus());
        userVO.setCreatedTime(user.getCreatedTime());
        return userVO;
    }

    /*
     * 对电子邮箱进行脱敏处理
     */
    private static String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 0) {
            return email;
        }
        // 显示@符号前的部分加上星号
        return "****" + email.substring(atIndex);
    }

    /*
     * 对电话进行脱敏处理
     */
    private static String maskPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return "";
        }
        // 对电话号码进行脱敏处理，例如只显示后四位数字
        int length = phone.length();
        if (length <= 4) {
            return phone;
        } else {
            return "****" + phone.substring(length - 4);
        }
    }
}
