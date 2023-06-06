package com.example.BlaBlaBackend.TrimNullValidator;


import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.entity.Ride;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;



public class TrimValidator  implements ConstraintValidator<Trim, Object> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String[] fields;
    private String[] excludeFields;
    private String messages;

    @Override
    public void initialize(Trim constraintAnnotation) {
        if(!Object.class.equals(String.class)) {
            Trim constraintAnnotation1 = (Trim) constraintAnnotation;
            messages = constraintAnnotation1.message();
            excludeFields = constraintAnnotation1.exclude();
            Arrays.sort(excludeFields);
        }else {

        }

//        System.out.println("\u001B[31m" + "Fields length =  " + fields.length + "\u001B[0m");
//        System.out.println("\u001B[31m" + "Exclude fieldslength = " + excludeFields.length + "\u001B[0m");

    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
//        System.out.println("\u001B[31m" + "Stringhere = " + value + "\u001B[0m");
        messages = "";
        if(fields.length == 0) {
            Field[] tempFields = value.getClass().getDeclaredFields();
            fields= Stream.of(tempFields).filter(data->(data.getType()==String.class)).map(data-> data.getName()).toArray(String[] :: new);
//            System.out.println("\u001B[31m" + "size = " + fields.length  + "\u001B[0m");
         }

        long notNull = Stream.of(fields)
                .map(field -> {
                    if(Arrays.binarySearch(excludeFields, field) < 0) {
//                        System.out.println("\u001B[36m" + "field value = " + (field) + "\u001B[0m");
                        try {
                            Field fields1 = value.getClass().getDeclaredField(field);
                            if((fields1.getType() == String.class)) {
                                fields1.setAccessible(true);
                                Object val = fields1.get(value);
//                                System.out.println("val = " + val);
                                if(val != null) {
                                    if(val.toString().trim() == ""){

                                        messages += (field + " is Empty,");
                                    }
                                    fields1.set(value, val.toString().trim());
                                } else {
                                    System.out.println("inside else");

                                   messages += (field + " is Null,");
                                }
                            }
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
//                        System.out.println("\u001B[36m" + "field value = " + PARSER.parseExpression(field).getValue(value) + "\u001B[0m");
                    }
                    return PARSER.parseExpression(field).getValue(value);} )
                .filter((data)->{
                    return Objects.nonNull(data) && !(data.equals(""));
                })
                .count();


//        System.out.println("\u001B[35m" + messages + "\u001B[0m");


        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messages).addConstraintViolation();
        return notNull == 0 || notNull == fields.length;
    }


}
