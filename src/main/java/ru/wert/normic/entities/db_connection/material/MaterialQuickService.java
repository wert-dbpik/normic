package ru.wert.normic.entities.db_connection.material;

import ru.wert.normic.entities.db_connection.CatalogService;
import ru.wert.normic.entities.db_connection.anyPart.AnyPartQuickService;

import java.util.ArrayList;
import java.util.List;

public class MaterialQuickService implements IMaterialService, CatalogService<Material> {

    private static MaterialQuickService instance;
    private static List<Material> materials;
    private static MaterialService service = MaterialService.getInstance();


    private MaterialQuickService() {
        reload();
    }

    public static MaterialQuickService getInstance() {
        if (instance == null)
            return new MaterialQuickService();
        return instance;
    }

    public static void reload(){
        while(true) {
            if(service != null) {
                materials = new ArrayList<>(service.findAll());
                break;
            }
        }
    }


    public Material findByName(String name){
        Material foundMaterial = null;
        for(Material material : materials){
            if(material.getName().equals(name)) {
                foundMaterial = material;
                break;
            }
        }
        return foundMaterial;
    }

    @Override //CatalogService
    public List<Material> findAllByGroupId(Long id) {
        List<Material> foundMaterials = new ArrayList<>();
        for(Material material : materials){
            if(material.getCatalogGroup().getId().equals(id)) {
                foundMaterials.add(material);
            }
        }
        return foundMaterials;
    }


    public Material findById(Long id) {
        Material foundMaterial = null;
        for(Material material : materials){
            if(material.getId().equals(id)) {
                foundMaterial = material;
                break;
            }
        }
        return foundMaterial;
    }

    @Override
    public Material save(Material material) {
        Material savedMaterial = service.save(material);
        reload();
        return savedMaterial;
    }

    @Override
    public boolean update(Material material) {
        boolean res = service.update(material);
        reload();
        return res;
    }

    @Override
    public boolean delete(Material material){
        boolean res = service.delete(material);
        reload();
        AnyPartQuickService.reload();
        return res;
    }

    public List<Material> findAll() {
        return materials;
    }

    public List<Material> findAllByText(String text) {
        List<Material> foundMaterials = new ArrayList<>();
        for(Material material : materials){
            String name = material.getName();
            if(name != null && name.contains(text)) {
                foundMaterials.add(material);
            }
        }
        return foundMaterials;
    }



}
