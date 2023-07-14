package com.javarush.jira.bugtracking.tree;

import com.javarush.jira.bugtracking.project.to.ProjectTo;
import com.javarush.jira.bugtracking.sprint.to.SprintTo;
import com.javarush.jira.bugtracking.task.to.TaskTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NodeMapper {
    @Mapping(target = "type", expression = "java(ObjectType.PROJECT)")
    NodeTo fromProject(ProjectTo project);

    @Mapping(target = "type", expression = "java(ObjectType.SPRINT)")
    @Mapping(target = "parentId", expression = "java(null)")
    NodeTo fromSprint(SprintTo sprint);

    @Mapping(target = "type", expression = "java(ObjectType.TASK)")
    NodeTo fromTask(TaskTo task);
}
