package com.fedstation.FedStation.service;

import javax.management.InvalidAttributeValueException;

import com.fedstation.FedStation.dto.NewProjectDto;
import com.fedstation.FedStation.entity.ModelType;
import com.fedstation.FedStation.entity.Project;
import com.fedstation.FedStation.entity.UserDetail;
import com.fedstation.FedStation.repository.ModelTypeRepo;
import com.fedstation.FedStation.repository.ProjectRepo;
import com.fedstation.FedStation.repository.UserDetailRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private ModelTypeRepo modelTypeRepo;

    @Autowired
    private UserDetailRepo userDetailRepo;

    @Override
    public Boolean checkProjectIdExists(String projectId) {
        return projectRepo.checkProjectIdExists(projectId);
    }

    @Override
    public void updateStatusById(String id, String field, Boolean value) {
        if (field.equals("isKeyDisabled")) {
            projectRepo.updateIsKeyDisabledById(value, id);
            return;
        }
        if (field.equals("isProjectDisabled")) {
            projectRepo.updateIsProjectDisabledById(value, id);
        }
    }

    @Override
    public void createNewProject(NewProjectDto project) throws InvalidAttributeValueException {
        ModelType mType = modelTypeRepo.findByModel(project.getModelType()).orElse(null);
        UserDetail userDetail = userDetailRepo.findById(project.getUserId()).orElse(null);
        if (mType == null || userDetail == null) {
            throw new InvalidAttributeValueException();
        }

        String projectKey = (new HelperServices()).generateKey();

        Project projectRecord = new Project(project.getId(), userDetail, project.getProjectName(),
                project.getProjectDescription(), projectKey, project.getMaxUsersSize(), mType, project.getStartAtTime(),
                project.getTriggerEvery());
        projectRepo.save(projectRecord);

    }

}