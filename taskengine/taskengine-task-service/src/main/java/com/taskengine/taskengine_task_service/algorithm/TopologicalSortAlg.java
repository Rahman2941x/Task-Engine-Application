package com.taskengine.taskengine_task_service.algorithm;

import com.taskengine.taskengine_task_service.model.Task;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TopologicalSortAlg {
    public List<Long> topologicalSort(List<Task> tasks,boolean usePriority) {

        Map<Long, List<Long>> graph = new HashMap<>();
        Map<Long, Integer> indegree = new HashMap<>();
        Map<Long,Task> taskMap= new HashMap<>();

        for (Task task : tasks) {
            indegree.put(task.getId(), 0);
            taskMap.put(task.getId(),task);
        }

        for (Task task : tasks) {
            if (task.getDependencies() != null) {
                for (Long dep : task.getDependencies()) {
                    graph.computeIfAbsent(dep, k -> new ArrayList<>()).add(task.getId());
                    indegree.put(task.getId(), indegree.get(task.getId()) + 1);
                }
            }
        }

        Queue<Task> queue;

        if(usePriority){
            queue=new PriorityQueue<>(
                    Comparator.comparing(Task::getPriority).reversed()
            );
        }else queue=new LinkedList<>();


        for (Task task :tasks) {
            if (indegree.get(task.getId()) == 0) queue.offer(task);
        }

        List<Long> order = new ArrayList<>();

        while (!queue.isEmpty()) {
            Task task = queue.poll();
            order.add(task.getId());

            for (Long next : graph.getOrDefault(task.getId(), List.of())) {
                indegree.put(next, indegree.get(next) - 1);

                if (indegree.get(next) == 0) {
                    queue.offer(taskMap.get(next));
                }
            }
        }

        return order;
    }
}
