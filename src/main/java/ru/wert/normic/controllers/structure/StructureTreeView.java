package ru.wert.normic.controllers.structure;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lombok.Getter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StructureTreeView {

    private StructureController controller;
    private TreeView<OpData> treeView;
    @Getter
    private TreeItem<OpData> root;
    private boolean treeExpanded;

    public StructureTreeView(StructureController controller, TreeView<OpData> treeView, TreeItem<OpData> root, boolean treeExpanded) {
        this.controller = controller;
        this.treeView = treeView;
        this.root = root;
        this.treeExpanded = treeExpanded;

        treeView.setRoot(root);
        root.setExpanded(true);

        treeView.setCellFactory(param -> new TreeViewCell(controller));
        buildTree(root);
        if(treeExpanded) expandTree(treeView, root);
    }
    public static Map<TreeItem<OpData>, Boolean> expandedItemsMap;

    public static Map<TreeItem<OpData>, Boolean> rememberExpended(TreeItem<OpData> item){
        expandedItemsMap.put(item, item.isExpanded());
        for(TreeItem<OpData> i : item.getChildren()){
            rememberExpended(i);
        }
        return expandedItemsMap;
    }

    public static void expandTreeItemsIfNeeded(TreeItem<OpData> item) {
        item.setExpanded(expandedItemsMap.get(item));
        for (TreeItem<OpData> i : item.getChildren()) {
            if (!expandedItemsMap.containsKey(i))
                expandedItemsMap.put(i, true);
            expandTreeItemsIfNeeded(i);
        }
    }

    /**
     * Построить дерево (инверсия)
     */
    public static void buildTree(TreeItem<OpData> treeItem){
        OpData opData = treeItem.getValue();
        List<OpData> operations = ((IOpWithOperations)opData).getOperations();

        for(OpData op : operations){
            if(op instanceof IOpWithOperations){
                TreeItem<OpData> newTreeItem = new TreeItem<>(op);
                treeItem.getChildren().add(newTreeItem);
                buildTree(newTreeItem);
            }
        }
    }

    /**
     * Раскрыть дерево
     */
    public static void expandTree(TreeView<OpData> treeView, TreeItem<OpData> root){
        int index = treeView.getFocusModel().getFocusedIndex();
        List<TreeItem<OpData>> allItems = findAllChildren(root);
        for(TreeItem<OpData> item : allItems){
            item.setExpanded(true);
        }
        treeView.getFocusModel().focus(index);
    }

    /**
     * Сложить дерево
     */
    public void foldTree(){
        List<TreeItem<OpData>> listOfItemsToUnfold = findAllChildren(root);
        for(TreeItem<OpData> ti : listOfItemsToUnfold)
            ti.setExpanded(false);
    }

    /**
     * Определение всех потомков элемента дерева, метод обобщенный, так как исп-ся для TreeTable
     */
    public static List<TreeItem<OpData>> findAllChildren(TreeItem<OpData> treeItem){
        //Создаем три листа
        //лист, где будут храниться, найденые потомки в текущей итерации
        List<TreeItem<OpData>> newList = new ArrayList<>();
        //лист со всеми найденными потомками
        List<TreeItem<OpData>> finalList = new ArrayList<>();
        //текущий лист для итерации
        List<TreeItem<OpData>> list = new ArrayList<>();
        //В него добавляем сам узел, от которого будем искать потомков
        list.add(treeItem);

        //В цикле получаем потомков текущего узла, складываем их во временный лист,
        //потом временный лист снова перебираем и для каждого узла находим своих потомков
        //Найденных потомков снова складываем в промежуточный лист, а предыдущий лист
        //суммируем с finalList.
        while(true){
            for(TreeItem<OpData> ti : list)
                newList.addAll(ti.getChildren());
            if(newList.isEmpty()) break;
            else {
                finalList.addAll(newList);
                list.clear();
                list.addAll(newList);
                newList.clear();
            }
        }
        return finalList;
    }
}
