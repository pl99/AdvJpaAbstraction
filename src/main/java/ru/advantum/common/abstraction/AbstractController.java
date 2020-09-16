package ru.advantum.common.abstraction;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.advantum.common.abstraction.AdvClassMetaData.Format;
import ru.advantum.common.abstraction.AdvFieldMetaData.VisibleType;

public abstract class AbstractController<D extends AbstractDto, S extends CommonService<D>>
        implements CommonController<D> {
    protected final S service;
    private static final String META_URL = "meta";
    private static final String DESCRIPTION = "description";

    protected AbstractController(S service) {
        this.service = service;
    }

    @Override
    @GetMapping("")
    public List<D> getAll() {
        return service.getAll();
    }

    @Override
    @GetMapping("find/{id}")
    public D findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Override
    @GetMapping("rx")
    public Flux<D> getRxAll() {
        return service.getRxAll();
    }

    @Override
    @GetMapping("rxfind/{id}")
    public Mono<D> findRxById(@PathVariable("id") Long id) {
        return service.findRxById(id);
    }

//    @SuppressWarnings("unchecked")
//    private final Class<D> dtoClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass())
//            .getActualTypeArguments()[0];
//    @GetMapping(value = META_URL)
//    public Map<String, Object> getMeta() {
//        Map<String, Object> metaData = new HashMap<>();
//        Map<String, Map> fieldsMap = new HashMap<>();
//
//        metaData.put(DESCRIPTION, dtoClass.getSimpleName());
//        if (dtoClass.isAnnotationPresent(JsonClassDescription.class)) {
//            JsonClassDescription classDescription = dtoClass.getAnnotation(JsonClassDescription.class);
//            metaData.put(DESCRIPTION, classDescription.value());
//        }
//        metaData.put("object", dtoClass.getSimpleName());
//        metaData.put("readOnly", false);
//        metaData.put("format", Format.ALL);
//        if (dtoClass.isAnnotationPresent(AdvClassMetaData.class)) {
//            Annotation annotation = dtoClass.getAnnotation(AdvClassMetaData.class);
//            AdvClassMetaData md = (AdvClassMetaData) annotation;
//            metaData.put("author", md.author());
//            metaData.put("version", md.version());
//            metaData.put("readOnly", md.readOnly());
//            metaData.put("format", md.format());
//        }
//
//        List<Field> fields = Arrays.asList(dtoClass.getSuperclass().getDeclaredFields());
//        fields.addAll(Arrays.asList(dtoClass.getDeclaredFields()));
//
//        for (Field field : fields/*dtoClass.getDeclaredFields()*/) {
//
//            field.setAccessible(true); // You might want to set modifier to public first.
//            Map<String, String> fieldMeta = new HashMap<>();
//            fieldsMap.put(field.getName(), fieldMeta);
//            String webFieldType = "";
//            switch (field.getType().getSimpleName()) {
//                case "String":
//                    webFieldType = "V";
//                    break;
//                case "Integer":
//                    webFieldType = "I";
//                    break;
//                case "Long":
//                    webFieldType = "I";
//                    break;
//                case "Double":
//                    webFieldType = "N";
//                    break;
//                case "Float":
//                    webFieldType = "N";
//                    break;
//                case "Timestamp":
//                    webFieldType = "D";
//                    break;
//                case "Date":
//                    webFieldType = "D";
//                    break;
//                case "LocalDate":
//                    webFieldType = "D";
//                    break;
//                default:
//                    webFieldType = "";
//            }
//            fieldMeta.put("javatype", field.getType().getSimpleName());
//            fieldMeta.put("type", webFieldType);
//
//            fieldMeta.put(DESCRIPTION, field.getName());
//            if (field.isAnnotationPresent(JsonPropertyDescription.class)) {
//                JsonPropertyDescription propertyDescription = field.getAnnotation(JsonPropertyDescription.class);
//                fieldMeta.put(DESCRIPTION, propertyDescription.value());
//            }
//            if (field.isAnnotationPresent(JsonFormat.class)) {
//                JsonFormat format = field.getAnnotation(JsonFormat.class);
//                fieldMeta.put("format.pattern", format.pattern());
//                fieldMeta.put("format.shape", format.shape().name());
//                fieldMeta.put("format.locale", format.locale());
//                fieldMeta.put("format.timezone", format.timezone());
//            }
//
//            //default values
//            fieldMeta.put("visible", String.valueOf(VisibleType.ALL));
//            if (field.isAnnotationPresent(AdvFieldMetaData.class)) {
//                AdvFieldMetaData fieldMetaData = field.getAnnotation(AdvFieldMetaData.class);
//                String title = fieldMetaData.title();
//                if (title.isEmpty()) {
//                    title = fieldMeta.get(DESCRIPTION);
//                }
//                fieldMeta.put("title", title);
//                fieldMeta.put("order", String.valueOf(fieldMetaData.order()));
//                fieldMeta.put("visible", String.valueOf(fieldMetaData.visible()));
//            }
//        }
//
//        metaData.put("fields", fieldsMap);
//        return metaData;
//    }

}
