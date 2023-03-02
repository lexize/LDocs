package org.lexize.ldocs;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.lexize.ldocs.gui.pages.LDocsClassPage;
import org.moon.figura.lua.docs.FiguraDoc;
import org.moon.figura.lua.docs.FiguraDocsManager;
import org.moon.figura.lua.docs.FiguraListDocs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class LDocs {
    private static final Logger LDOCS_LOGGER = Logger.getLogger("LDocs");
    private static final List<LDocsElement> loadedElements = new ArrayList<>();
    private static LDocsElement selectedElement;
    public static void init() {
        var classesDocs = getFiguraDocs();
        LDocsElement globalsElement = new LDocsElement();
        globalsElement.setElementTitle(Component.translatable("ldocs.tree.globals"));
        LDocsElement classElement = new LDocsElement();
        classElement.setElementTitle(Component.translatable("ldocs.tree.classes"));
        assert classesDocs != null;
        for (Map.Entry<String, List<FiguraDoc.ClassDoc>> kv :
                classesDocs.entrySet()) {
            LDocsElement e = new LDocsElement();
            e.setElementTitle(Component.literal(kv.getKey()));
            for (FiguraDoc.ClassDoc cd :
                    kv.getValue()) {
                LDocsElement ce = new LDocsElement();
                ce.setElementTitle(Component.literal(FiguraDocsManager.getNameFor(cd.thisClass)));
                ce.setElementPage(new LDocsClassPage(0,0,1,1,cd));
                e.getChildren().add(ce);
                classElement.getChildren().add(ce);
            }
            globalsElement.getChildren().add(e);
        }
        loadedElements.add(globalsElement);
        loadedElements.add(classElement);
    }

    public static Logger getLdocsLogger() {
        return LDOCS_LOGGER;
    }

    public static LDocsElement getSelectedElement() {
        return selectedElement;
    }

    public static void setSelectedElement(LDocsElement selectedElement) {
        LDocs.selectedElement = selectedElement;
    }

    public static List<LDocsElement> getLoadedElements() {
        return loadedElements;
    }
    public static Map<String, List<FiguraDoc.ClassDoc>> getFiguraDocs() {
        try {
            Class<FiguraDocsManager> fdmClass = FiguraDocsManager.class;
            Field generatedDocsField = fdmClass.getDeclaredField("GENERATED_CHILDREN");
            generatedDocsField.trySetAccessible();
            return (Map<String, List<FiguraDoc.ClassDoc>>) generatedDocsField.get(null);
        } catch (Exception e) {
            return null;
        }
    }
    public static Map<String, LListDocs> getFiguraListDocs() {
        Map<String, LListDocs> vals = new HashMap<>();
        try {
            Class<?> c = Class.forName("org.moon.figura.lua.docs.FiguraListDocs.ListDoc");
            Field idField = c.getDeclaredField("id"); idField.trySetAccessible();
            Field nameField = c.getDeclaredField("name"); nameField.trySetAccessible();
            Field splitField = c.getDeclaredField("split"); splitField.trySetAccessible();
            Method supplierMethod = c.getDeclaredMethod("get"); supplierMethod.trySetAccessible();
            for (Object o :
                    c.getEnumConstants()) {
                String id, name;
                id = (String) idField.get(o);
                name = (String) nameField.get(o);
                int split = (Integer) splitField.get(o);
                Collection<?> col = (Collection<?>) supplierMethod.invoke(o);
                vals.put(id, new LListDocs(col, split, id, name));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vals;
    }
    public static class Utils {
        public static Component formattedTextToComponent(FormattedText text) {
            return text.visit(new FormattedTextToComponentVisitor(), Style.EMPTY).orElse(null);
        }
        public static class FormattedTextToComponentVisitor implements FormattedText.StyledContentConsumer<Component> {
            private final MutableComponent component = Component.empty();
            @Override
            public @NotNull Optional<Component> accept(Style style, String string) {
                MutableComponent appendableComponent = Component.literal(string);
                appendableComponent.setStyle(style);
                component.append(appendableComponent);
                return Optional.of(component);
            }
        }
    }
}
