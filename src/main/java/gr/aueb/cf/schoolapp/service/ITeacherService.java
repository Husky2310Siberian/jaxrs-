package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;

import java.util.List;
import java.util.Map;

public interface ITeacherService {

    TeacherReadOnlyDTO insertTeacher(TeacherInsertDTO teacherInsertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    TeacherReadOnlyDTO updateTeacher(TeacherUpdateDTO teacherUpdateDTO) throws EntityNotFoundException, EntityInvalidArgumentException;

    void deleteTeacher(Object id) throws EntityNotFoundException;

    TeacherReadOnlyDTO getTeacherById(Object id) throws EntityNotFoundException;

    List<TeacherReadOnlyDTO> getAllTeachers();

    //returns null list
    List<TeacherReadOnlyDTO> getAllTeachersByCriteria(Map<String , Object> criteria);
}
