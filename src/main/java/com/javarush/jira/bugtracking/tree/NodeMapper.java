package com.javarush.jira.bugtracking.tree;

import com.javarush.jira.bugtracking.project.to.ProjectDTO;
import com.javarush.jira.bugtracking.sprint.to.SprintDTO;
import com.javarush.jira.bugtracking.task.to.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NodeMapper {
    @Mapping(target = "type", expression = "java(ObjectType.PROJECT)")
    NodeDTO fromProject(ProjectDTO project);

    @Mapping(target = "type", expression = "java(ObjectType.SPRINT)")
    @Mapping(target = "parentId", expression = "java(null)")
    NodeDTO fromSprint(SprintDTO sprint);

    @Mapping(target = "type", expression = "java(ObjectType.TASK)")
    NodeDTO fromTask(TaskDTO task);
}
