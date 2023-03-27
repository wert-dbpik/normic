package ru.wert.normic.controllers.extra.tree_view;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import ru.wert.normic.controllers.extra.StructureController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.ArrayList;
import java.util.List;

public class StructureTreeView {

    private StructureController controller;
    private TreeView<OpData> treeView;
    private TreeItem<OpData> rootItem;

    public StructureTreeView(StructureController controller, TreeView<OpData> treeView, OpAssm opRoot) {
        this.controller = controller;
        this.treeView = treeView;

        rootItem = new TreeItem<>(opRoot);

        treeView.setRoot(rootItem);
        if(((OpAssm)rootItem.getValue()).getOperations().isEmpty())
            treeView.setShowRoot(false);

        treeView.setCellFactory(param -> new TreeViewCell(controller));
        buildTree(rootItem);
    }

    private void buildTree(TreeItem<OpData> treeItem){
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

    public void unfoldTree(){
        int index = treeView.getFocusModel().getFocusedIndex();
        List<TreeItem<OpData>> allItems = findAllChildren(rootItem);
        for(TreeItem<OpData> item : allItems){
            item.setExpanded(true);
        }
        treeView.getFocusModel().focus(index);
    }

    public void foldTree(){
        List<TreeItem<OpData>> listOfItemsToUnfold = findAllChildren(rootItem);
        for(TreeItem<OpData> ti : listOfItemsToUnfold)
            ti.setExpanded(false);
    }

    /**
     * Определение всех потомков элемента дерева, метод обобщенный, так как исп-ся для TreeTable
     */
    public List<TreeItem<OpData>> findAllChildren(TreeItem<OpData> treeItem){
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
