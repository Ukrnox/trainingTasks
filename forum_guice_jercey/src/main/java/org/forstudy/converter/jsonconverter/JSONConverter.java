package org.forstudy.converter.jsonconverter;


import org.forstudy.entities.Group;
import org.forstudy.entities.Post;
import org.forstudy.entities.User;
import org.forstudy.converter.MyJSONIgnore;
import org.forstudy.converter.ObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.*;

public class JSONConverter implements ObjectMapper<JSONConverter.JSON, Object> {

    @Override
    public <T> T convertFrom(JSON json, Class<T> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Map<String, Object> map = json.getMap();
//        Field f = Unsafe.class.getDeclaredField("theUnsafe"); //Internal reference
//        f.setAccessible(true);
//        Unsafe unsafe = (Unsafe) f.get(null);

        //This creates an instance of player class without any initialization
//        T t = (T) unsafe.allocateInstance(tClass);

        //This creates an instance of player class without any initialization
//        Player p = (Player) unsafe.allocateInstance(Player.class);

        T t = tClass.getDeclaredConstructor().newInstance();
        Field[] declaredFields = tClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String fieldName = declaredField.getName();
            Object fieldValue = map.get(fieldName);
            if (fieldValue != null && !fieldValue.equals("null")) {
                Field field = tClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                if (fieldValue instanceof JSON) {
//                System.out.println(field);
                    field.set(t, convertFrom((JSON) fieldValue, declaredField.getType()));
                }
                else {
                    field.set(t, fieldValue);
                }
            }
        }
        return t;
    }

    @Override
    public JSON convertTo(Object o) throws IllegalAccessException {
        if (o != null) {
            Class<?> oClass = o.getClass();
            Field[] fields = oClass.getDeclaredFields();
            JSON json = new JSON();
            Map<String, Object> map = json.getMap();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (!Modifier.isStatic(field.getModifiers()) &&
                        !field.isAnnotationPresent(MyJSONIgnore.class)) {
                    field.setAccessible(true);
                    Object value = field.get(o);
                    if (value instanceof String || value instanceof Number) {
                        map.put(field.getName(), value);
                    }
                    else if (value instanceof Iterable) {
                        map.put(field.getName(), collectionMapper((Iterable<Object>) value));
                    }
                    else if (value instanceof Map) {
                        map.put(field.getName(), value.toString());
                    }
                    else if (value == null) {
                        map.put(field.getName(), "null");
                    }
                    else {
                        map.put(field.getName(), convertTo(value));
                    }
                }
            }
            return json;
        }
        return null;
    }

    private String collectionMapper(Iterable<Object> iterable) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object next : iterable) {
            if (next instanceof Iterable) {
                collectionMapper((Iterable<Object>) next);
            }
            else {
                try {
                    stringBuilder.append(convertTo(next));
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public class JSON {
        private final Map<String, Object> map = new HashMap<>();

        public Map<String, Object> getMap() {
            return map;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                sb.append(key).append(":");
                if (value == null) {
                    sb.append("null");
                }
                else {
                    sb.append(value);
                }
                if (iterator.hasNext()) {
                    sb.append(",");
                }
            }
            sb.append("}");
            return sb.toString();
        }

    }

    public static void main(String[] args) throws Exception {
        JSONConverter jc = new JSONConverter();
        User user = new User();
        Group group = new Group();
        group.setName("Group");
        Group group2 = new Group();
        group2.setName("Group2");
        Group group3 = new Group();
        group3.setName("Group3");
        Set<Group> groups = new HashSet<>();
        groups.add(group2);
        groups.add(group);

        Post post = new Post();
        post.setText("SomeText");
        post.setDateCreated(LocalDateTime.now());
        Set<Post> posts = new HashSet<>();
        posts.add(post);
        user.setRegistrationDate(LocalDateTime.now());
        user.setGroups(groups);
        user.setLogin("adaw");
        user.setPosts(posts);

        user.setPassword("1213");
        user.setId(1L);
        System.out.println(jc.convertTo(user));
    }
}
