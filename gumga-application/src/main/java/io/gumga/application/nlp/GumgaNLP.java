package io.gumga.application.nlp;

import io.gumga.core.GumgaValues;
import io.gumga.domain.nlp.GumgaNLPThing;
import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.text.*;
import org.cogroo.text.impl.DocumentImpl;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Component
public class GumgaNLP {

    public static final String NO_NAME = "_NO_NAME";
    private Analyzer cogroo;
    private Reflections reflections;
    private Set<Class<?>> classOfInterest;

    @Autowired
    public GumgaNLP(GumgaValues gumgaValues) {
        String basePackage = gumgaValues.getGumgaNLPBasePackage();
        try {
            ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
            cogroo = factory.createPipe();
            reflections = new Reflections(basePackage);
            System.out.println("ReflectionsConfiguration------->" + reflections.getConfiguration().getUrls());
            classOfInterest = reflections.getTypesAnnotatedWith(GumgaNLPThing.class);
            System.out.println("GumgaNLP ------" + basePackage + "----->" + classOfInterest);
        } catch (Exception ex) {
            System.out.println("GumgaNLP ------" + basePackage + "-----> NOT WORKING IN THIS SYSTEM " + ex);
        }
    }

    private enum Estados {
        VERBO, SUBSTANTIVO, FIM, ATRIBUTOS, VALOR_ATRIBUTO
    };

    public List<Object> createObjectsFromDocument(String text, String instanceVerbs) throws Exception {
        List<String> verbs = Arrays.asList(instanceVerbs.split(","));
        Document document = new DocumentImpl();
        document.setText(text);
        cogroo.analyze(document);
        print(document);
        ArrayList<Object> toReturn = new ArrayList<>();
        Object currentObject = null;
        Field currentField = null;
        Estados estado = Estados.VERBO;

        for (Sentence sentence : document.getSentences()) {
            int i = 0;
            List<Token> tokens = sentence.getTokens();
            while (i < tokens.size()) {
                System.out.print(estado + " ");
                Token token = sentence.getTokens().get(i);
                if (token.getPOSTag().startsWith("v-")) {
                    for (String v : verbs) {
                        if (Arrays.asList(token.getLemmas()).contains(v)) {
                            estado = Estados.SUBSTANTIVO;
                        }
                    }
                } else if (estado == Estados.SUBSTANTIVO && token.getPOSTag().startsWith("n")) {
                    Class classe = null;
                    for (Class c : classOfInterest) {
                        GumgaNLPThing gumgaNPLThing = (GumgaNLPThing) c.getAnnotation(GumgaNLPThing.class);
                        String nome = (NO_NAME.equals(gumgaNPLThing.value()) ? c.getSimpleName() : gumgaNPLThing.value()).toLowerCase();
                        if (nome.equals(token.getLexeme())) {
                            currentObject = c.newInstance();
                            toReturn.add(currentObject);
                            estado = Estados.ATRIBUTOS;
                        }
                    }
                } else if (estado == Estados.ATRIBUTOS && token.getPOSTag().startsWith("n")) {
                    try {
                        currentField = mapAllFields(currentObject.getClass()).get(token.getLexeme());  //currentObject.getClass().getDeclaredField(token.getLexeme());tod
                        estado = Estados.VALOR_ATRIBUTO;
                    } catch (NullPointerException nfe) {

                    }
                } else if (estado == Estados.VALOR_ATRIBUTO && (!token.getPOSTag().startsWith("prp"))) {
                    try {
                        currentField.setAccessible(true);
                        currentField.set(currentObject, currentField.getType().getConstructor(String.class).newInstance(token.getLexeme().toLowerCase()));
                        estado = Estados.ATRIBUTOS;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                i++;
            }

        }
        System.out.println("---------->" + toReturn);
        return toReturn;

    }

    private Map<String, Field> mapAllFields(Class clazz) {
        Map<String, Field> toReturn;
        if (clazz.getSuperclass().equals(Object.class)) {
            toReturn = new HashMap<>();
        } else {
            toReturn = mapAllFields(clazz.getSuperclass());
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields) {
            String label = f.getName();
            if (f.isAnnotationPresent(GumgaNLPThing.class)) {
                GumgaNLPThing annotation = f.getAnnotation(GumgaNLPThing.class);
                label = annotation.value();
            }
            toReturn.put(label, f);
        }
        return toReturn;

    }

    private void print(Document document) {
        StringBuilder output = new StringBuilder();
        for (Sentence sentence : document.getSentences()) {
            output.append("Sentence: ").append(sentence.getText()).append("\n");
            output.append("  Tokens: \n");
            for (Token token : sentence.getTokens()) {
                String lexeme = token.getLexeme();
                String lemmas = Arrays.toString(token.getLemmas());
                String pos = token.getPOSTag();
                String feat = token.getFeatures();
                output.append(String.format("    %-10s %-12s %-6s %-10s\n", lexeme, lemmas, pos, feat));
            }
            output.append("  Chunks: ");
            for (Chunk chunk : sentence.getChunks()) {
                output.append("[").append(chunk.getTag()).append(": ");
                for (Token innerToken : chunk.getTokens()) {
                    output.append(innerToken.getLexeme()).append(" ");
                }
                output.append("] ");
            }
            output.append("\n");
            output.append("  Shallow Structure: ");
            for (SyntacticChunk structure : sentence.getSyntacticChunks()) {
                output.append("[").append(structure.getTag()).append(": ");
                for (Token innerToken : structure.getTokens()) {
                    output.append(innerToken.getLexeme()).append(" ");
                }
                output.append("] ");
            }
            output.append("\n");
        }
        System.out.println(output.toString());
    }

}
