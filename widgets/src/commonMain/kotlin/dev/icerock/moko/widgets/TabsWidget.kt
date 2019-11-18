/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var tabsWidgetViewFactory: VFC<TabsWidget>

@WidgetDef
class TabsWidget(
    val factory: VFC<TabsWidget>,
    override val style: Style,
    override val id: Id?,
    override val layoutParams: LayoutParams,
    val tabs: List<TabsWidget.Tab>
) : Widget(), Styled<TabsWidget.Style>, OptionalId<TabsWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    class Tab(
        val title: LiveData<StringDesc>,
        val body: Widget
    )

    data class LayoutParams(
        override val size: WidgetSize = WidgetSize.Const(
            width = SizeSpec.WRAP_CONTENT,
            height = SizeSpec.WRAP_CONTENT
        ),
        override val margins: MarginValues? = null,
        override val padding: PaddingValues? = null
    ) : Widget.LayoutParams, Margined, Padded

    data class Style(
        override val background: Background? = null
    ) : Widget.Style, Backgrounded

    interface Id : WidgetScope.Id
}
