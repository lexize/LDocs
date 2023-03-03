package org.lexize.ldocs.gui.pages;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.lexize.ldocs.LDocs;
import org.lexize.ldocs.gui.components.LDocsLabel;
import org.lexize.ldocs.gui.components.LDocsWrappedLabel;
import org.lexize.ldocs.utils.LDocsClickEvent;
import org.moon.figura.lua.docs.FiguraDoc;
import org.moon.figura.lua.docs.FiguraDocsManager;
import org.moon.figura.utils.FiguraText;

import java.util.ArrayList;
import java.util.List;

public class LDocsClassPage extends LDocsPage {
    private static final int DESCRIPTION_PADDING_X = 5;
    private static final int DESCRIPTION_PADDING_Y = 5;
    private static final int FUNCTION_PADDING_Y = 5;
    private static final int FIELD_PADDING_Y = 5;
    private final List<AbstractWidget> widgets = new ArrayList<>();
    private final List<MethodBox> methodBoxes = new ArrayList<>();
    private final List<FieldBox> fieldBoxes = new ArrayList<>();
    private final LDocsLabel classNameLabel;
    private LDocsLabel extendsSuperclassLabel = null;
    private LDocsLabel functionsLabel;
    private LDocsLabel fieldsLabel;
    private final LDocsWrappedLabel descriptionLabel;
    public LDocsClassPage(int x, int y, int width, int height,FiguraDoc.ClassDoc doc) {
        super(x, y, width, height, Component.literal(FiguraDocsManager.getNameFor(doc.thisClass)));
        int yOffset;
        classNameLabel = new LDocsLabel(5,5, Component.literal(FiguraDocsManager.getNameFor(doc.thisClass)));
        classNameLabel.setTextSize(3);
        yOffset = classNameLabel.getY() + classNameLabel.getHeight();
        Class<?> superClass = doc.superclass;
        if (superClass != null) {
            extendsSuperclassLabel = new LDocsLabel(5, yOffset, FiguraText.of("docs.text.extends").append(" ")
                    .append(getClassText(superClass).withStyle(ChatFormatting.YELLOW)));
            yOffset += extendsSuperclassLabel.getHeight();
            widgets.add(extendsSuperclassLabel);
        }
        widgets.add(classNameLabel);
        descriptionLabel = new LDocsWrappedLabel(DESCRIPTION_PADDING_X,
                yOffset+DESCRIPTION_PADDING_Y, width-(DESCRIPTION_PADDING_X*2),
                FiguraText.of("docs."+doc.description));
        widgets.add(descriptionLabel);
        yOffset += descriptionLabel.getHeight() + DESCRIPTION_PADDING_Y;
        if (doc.documentedFields.size() > 0) {
            fieldsLabel = new LDocsLabel(5, yOffset, Component.translatable("ldocs.pages.class.fields"));
            fieldsLabel.setTextSize(2);
            widgets.add(fieldsLabel);
            yOffset += fieldsLabel.getHeight() + FIELD_PADDING_Y;
            for (var f :
                    doc.documentedFields) {
                var fb = new FieldBox(5,yOffset,width-10, f);
                fieldBoxes.add(fb);
                widgets.add(fb);
                yOffset += fb.getHeight() + FIELD_PADDING_Y;
            }

        }
        if (doc.documentedMethods.size() > 0) {
            functionsLabel = new LDocsLabel(5, yOffset, Component.translatable("ldocs.pages.class.functions"));
            functionsLabel.setTextSize(2);
            widgets.add(functionsLabel);
            yOffset += functionsLabel.getHeight() + FUNCTION_PADDING_Y;
            for (var m :
                    doc.documentedMethods) {
                var mb = new MethodBox(5, yOffset, width-(10), m);
                methodBoxes.add(mb);
                widgets.add(mb);
                yOffset += mb.getHeight()+FUNCTION_PADDING_Y;
            }
        }
    }

    @Override
    public int getHeight() {
        int maxY = getY();
        for (var w :
                widgets) {
            maxY = Math.max(w.getY() + w.getHeight(), maxY);
        }
        return maxY - getY();
    }

    @Override
    protected List<? extends AbstractWidget> getContainedChildren() {
        return widgets;
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        int yOffset = classNameLabel.getY() + classNameLabel.getHeight();
        if (extendsSuperclassLabel != null) {
            extendsSuperclassLabel.setY(yOffset);
            yOffset += extendsSuperclassLabel.getHeight();
        }
        descriptionLabel.setY(yOffset);
        descriptionLabel.setWidth(width-(DESCRIPTION_PADDING_X*2));
        yOffset += descriptionLabel.getHeight() + DESCRIPTION_PADDING_Y;
        if (fieldBoxes.size() > 0) {
            fieldsLabel.setY(yOffset);
            yOffset += fieldsLabel.getHeight() + FIELD_PADDING_Y;
            for (var f :
                    fieldBoxes) {
                f.setWidth(width-10);
                f.setY(yOffset);
                yOffset += f.getHeight()+FIELD_PADDING_Y;
            }
        }
        if (methodBoxes.size() > 0) {
            functionsLabel.setY(yOffset);
            yOffset += functionsLabel.getHeight() + FUNCTION_PADDING_Y;
            for (var m :
                    methodBoxes) {
                m.setWidth(width-10);
                m.setY(yOffset);
                yOffset += m.getHeight()+FUNCTION_PADDING_Y;
            }
        }
        super.renderButton(matrices, mouseX, mouseY, delta);
    }
    private static MutableComponent getClassText(Class<?> c) {
        var text = FiguraDocsManager.getClassText(c);
        var style = text.getStyle().withHoverEvent(null).withClickEvent(null);
        var e = LDocs.classesToElements.get(c);
        if (e != null) {
            var r = LDocs.elementsToRunnables.get(e);
            style = style.withClickEvent(new LDocsClickEvent(r));
        }
        text.setStyle(style);
        return text;
    }
    static class FieldBox extends AbstractContainerWidget {
        private final List<AbstractWidget> widgets = new ArrayList<>();
        private final LDocsWrappedLabel fieldDescriptor;
        private final LDocsLabel editableLabel;
        private final LDocsWrappedLabel descriptionLabel;
        public FieldBox(int x, int y, int width, FiguraDoc.FieldDoc f) {
            super(x, y, width, 0, Component.empty());
            int yOffset;
            MutableComponent fieldDescriptorComponent = Component.empty();
            fieldDescriptorComponent.append(getClassText(f.type).withStyle(ChatFormatting.YELLOW));
            fieldDescriptorComponent.append(" " + f.name);
            fieldDescriptor = new LDocsWrappedLabel(x,y, width,fieldDescriptorComponent);
            widgets.add(fieldDescriptor);
            yOffset = fieldDescriptor.getY() + fieldDescriptor.getHeight() + 1;
            var editableLabelComponent = FiguraText.of(f.editable ? "docs.text.editable" : "docs.text.not_editable")
                    .withStyle(f.editable ? ChatFormatting.GREEN : ChatFormatting.DARK_RED);
            editableLabel = new LDocsLabel(x, yOffset, editableLabelComponent);
            widgets.add(editableLabel);
            yOffset += editableLabel.getHeight() + 1;
            descriptionLabel = new LDocsWrappedLabel(x,yOffset, width, FiguraText.of("docs."+f.description));
            widgets.add(descriptionLabel);
        }

        @Override
        public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
            int yOffset;
            fieldDescriptor.setX(getX());
            fieldDescriptor.setY(getY());
            fieldDescriptor.setWidth(width);
            yOffset = fieldDescriptor.getY() + fieldDescriptor.getHeight() + 1;
            editableLabel.setX(getX());
            editableLabel.setY(yOffset);
            yOffset += editableLabel.getHeight() + 1;
            descriptionLabel.setX(getX());
            descriptionLabel.setY(yOffset);
            descriptionLabel.setWidth(width);

            super.renderButton(matrices, mouseX, mouseY, delta);
        }

        @Override
        public int getHeight() {
            int maxY = getY();
            for (var w :
                    widgets) {
                maxY = Math.max(w.getY() + w.getHeight(), maxY);
            }
            return maxY - getY();
        }

        @Override
        protected List<? extends AbstractWidget> getContainedChildren() {
            return widgets;
        }
    }
    static class MethodBox extends AbstractContainerWidget {
        private final List<AbstractWidget> widgets = new ArrayList<>();
        private final LDocsLabel titleLable;
        private final LDocsWrappedLabel descriptionLabel;
        private final ArrayList<LDocsWrappedLabel> overloadStrings = new ArrayList<>();
        public MethodBox(int x, int y, int width, FiguraDoc.MethodDoc m) {
            super(x,y,width,0, Component.empty());
            int yOffset;
            titleLable = new LDocsLabel(x,y,Component.literal(m.name).withStyle(ChatFormatting.YELLOW));
            widgets.add(titleLable);
            yOffset = titleLable.getY() + titleLable.getHeight() + 1;
            for (int i = 0; i < m.parameterNames.length; i++) {
                String[] argsNames = m.parameterNames[i];
                Class<?>[] argsTypes = m.parameterTypes[i];
                Class<?> returnType = m.returnTypes[i];
                MutableComponent c = Component.literal(m.name);
                c.append("(");
                for (int j = 0; j < argsNames.length; j++) {
                    c.append(getClassText(argsTypes[j]).withStyle(ChatFormatting.YELLOW));
                    c.append(" " + argsNames[j]);
                    if (j < argsNames.length - 1) c.append(", ");
                }
                c.append(") ");
                c.append(FiguraText.of("docs.text.returns")).append(" ").append(
                        getClassText(returnType).withStyle(ChatFormatting.YELLOW)
                );
                LDocsWrappedLabel overloadLabel = new LDocsWrappedLabel(x+5, yOffset, width-5, c);
                overloadStrings.add(overloadLabel);
                widgets.add(overloadLabel);
                yOffset += overloadLabel.getHeight() + 1;
            }
            descriptionLabel = new LDocsWrappedLabel(x+5, yOffset, width, FiguraText.of("docs."+m.description));
            widgets.add(descriptionLabel);
        }

        @Override
        public int getHeight() {
            int maxY = getY();
            for (var w :
                    widgets) {
                maxY = Math.max(w.getY() + w.getHeight(), maxY);
            }
            return maxY - getY();
        }

        @Override
        public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
            titleLable.setX(getX());
            titleLable.setY(getY());
            int yOffset = titleLable.getY() + titleLable.getHeight() + 1;
            for (var s :
                    overloadStrings) {
                s.setX(getX() + 5);
                s.setY(yOffset);
                s.setWidth(width-5);
                yOffset += s.getHeight() + 1;
            }
            descriptionLabel.setX(getX()+ 5);
            descriptionLabel.setY(yOffset);
            descriptionLabel.setWidth(width-5);
            super.renderButton(matrices, mouseX, mouseY, delta);
        }

        @Override
        protected List<? extends AbstractWidget> getContainedChildren() {
            return widgets;
        }
    }
}
