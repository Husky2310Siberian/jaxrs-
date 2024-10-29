package gr.aueb.cf.schoolapp.service;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import gr.aueb.cf.schoolapp.core.exceptions.AppServerException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.UserInsertDTO;
import gr.aueb.cf.schoolapp.dto.UserReadOnlyDTO;

public interface IUserService {

    UserReadOnlyDTO insertUser(UserInsertDTO dto) throws EntityNotFoundException , AppServerException;
    UserReadOnlyDTO getUserByUsername(String username) throws EntityNotFoundException;
    boolean isUserValid (String username, String password);
    boolean isEmailExists (String username);
}
