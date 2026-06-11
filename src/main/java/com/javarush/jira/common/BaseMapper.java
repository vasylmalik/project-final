package com.javarush.jira.common;

import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.bugtracking.task.to.TaskTo;
import com.javarush.jira.common.to.BaseTo;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface BaseMapper<E, T extends BaseTo> {

    E toEntity(T to);

    List<E> toEntityList(Collection<T> tos);

    E updateFromTo(T to, @MappingTarget E entity);

    T toTo(E entity);

    List<T> toToList(Collection<E> entities);

    List<TaskTo> toList(ArrayList<Task> tasks);

    Set<TaskTo> toSet(Set<Task> tasks);
}
