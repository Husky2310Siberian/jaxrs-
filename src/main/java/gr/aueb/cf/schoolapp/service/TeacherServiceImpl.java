package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Teacher;

import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TeacherServiceImpl implements ITeacherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);

    //make teacherDAO final , to ensure it is injected
    private final ITeacherDAO teacherDAO;

    @Override
    public TeacherReadOnlyDTO insertTeacher(TeacherInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Teacher teacher = Mapper.mapToTeacher(insertDTO);

            // Insert is not Idempotent, is not unchangeable
            if (teacherDAO.getByVat(insertDTO.getVat()).isPresent()) {
                throw new EntityAlreadyExistsException("Teacher", "Teacher with vat: " + insertDTO.getVat()
                        + "not inserted");
            }
//            teacherDAO.getByVat(insertDTO.getVat()).orElseThrow(() -> new EntityAlreadyExistsException("Teacher", "Teacher with vat: " +
//                    insertDTO.getVat() + " already exists"));

            TeacherReadOnlyDTO readOnlyDTO = teacherDAO.insert(teacher)
                    .map(Mapper::mapToTeacherReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Teacher", " Teacher with vat: "
                            + insertDTO.getVat() + " not inserted"));
            JPAHelper.commitTransaction();
            LOGGER.info("Teacher with id {} , vat with {},  firstname {} , lastname {} inserted",
                    teacher.getId(), teacher.getVat(), teacher.getFirstname(), teacher.getLastname());
            return readOnlyDTO;
        } catch (Exception e) {
            JPAHelper.rollbackTansaction();
            LOGGER.error("Error , Teacher with vat: {} ,  firstname: {} and lastname : {} not inserted",
                    insertDTO.getVat(), insertDTO.getFirstname(), insertDTO.getLastname());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public TeacherReadOnlyDTO updateTeacher(TeacherUpdateDTO updateDTO)
            throws EntityNotFoundException, EntityInvalidArgumentException {

        try {
            JPAHelper.beginTransaction();
            Teacher teacher = Mapper.mapToTeacher(updateDTO);

//            if(teacherDAO.getByVat(updateDTO.getVat()).isEmpty()){
//                throw new EntityNotFoundException("Teacher","Teacher with vat:" + updateDTO.getVat() +" not found");
//            }

            teacherDAO.getByVat(updateDTO.getVat()).orElseThrow(() -> new EntityNotFoundException("Teacher", "Teacher with vat:"
                    + updateDTO.getVat() + " not found"));
            TeacherReadOnlyDTO readOnlyDTO = teacherDAO.update(teacher)
                    .map(Mapper::mapToTeacherReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Teacher", "Error during update"));
            JPAHelper.commitTransaction();
            LOGGER.info("Teacher with id {} ,vat: {}, lastname {} , firstname {} updated",
                    teacher.getId(), teacher.getVat(), teacher.getLastname(), teacher.getFirstname());
            return readOnlyDTO;
        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            JPAHelper.rollbackTansaction();
            LOGGER.error("Error , Teacher not updated: id {}, vat: {} , firstname: {} , lastname: {} not updated",
                    updateDTO.getId(), updateDTO.getVat(), updateDTO.getFirstname(), updateDTO.getLastname());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public void deleteTeacher(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            teacherDAO.getById(id).orElseThrow(() -> new EntityNotFoundException("Teacher", "Teacher with id: "
                    + id + "not found"));
            teacherDAO.delete(id);
            JPAHelper.commitTransaction();
            LOGGER.info("Teacher with id {} was deleted", id);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTansaction();
            LOGGER.info("Error, Teacher with id: {} was not deleted", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public TeacherReadOnlyDTO getTeacherById(Object id) throws EntityNotFoundException {

        try {
            JPAHelper.beginTransaction();

            TeacherReadOnlyDTO readOnlyDTO = teacherDAO.getById(id)
                    .map(Mapper::mapToTeacherReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher", "Teacher with id "
                            + id + "not found"));
            JPAHelper.commitTransaction();
            LOGGER.info("Teacher with id {} was not found", id);
            return readOnlyDTO;
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTansaction();
            LOGGER.warn("Error. Teacher with id {} was not found", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<TeacherReadOnlyDTO> getAllTeachers() {
        try{
            JPAHelper.beginTransaction();
            List<TeacherReadOnlyDTO> readOnlyDTOS = teacherDAO.getAll()
                            .stream()
                            .map(Mapper::mapToTeacherReadOnlyDTO)
                            .toList();
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<TeacherReadOnlyDTO> getAllTeachersByCriteria(Map<String, Object> criteria) {

        try{
            JPAHelper.beginTransaction();
            List<TeacherReadOnlyDTO> readOnlyDTOs = teacherDAO.getByCriteria(criteria)
                    .stream()
                    .map(Mapper::mapToTeacherReadOnlyDTO)
                    .toList();
            JPAHelper.commitTransaction();
            return readOnlyDTOs;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
