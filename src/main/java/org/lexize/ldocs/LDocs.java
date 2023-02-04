package org.lexize.ldocs;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LDocs {
    private static LDocsElement selectedElement;

    public static LDocsElement getSelectedElement() {
        return selectedElement;
    }

    public static void setSelectedElement(LDocsElement selectedElement) {
        LDocs.selectedElement = selectedElement;
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
