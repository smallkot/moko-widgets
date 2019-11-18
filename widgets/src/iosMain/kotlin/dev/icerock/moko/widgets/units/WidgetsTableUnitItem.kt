/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.plural.getAssociatedObject
import dev.icerock.plural.setAssociatedObject
import platform.UIKit.UIApplication
import platform.UIKit.UITableView
import platform.UIKit.UITableViewCell
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leftAnchor
import platform.UIKit.rightAnchor
import platform.UIKit.topAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual abstract class WidgetsTableUnitItem<T> actual constructor(override val itemId: Long, val data: T) :
    TableUnitItem {
    actual abstract val reuseId: String
    actual abstract fun createWidget(data: LiveData<T>): Widget

    override val reusableIdentifier: String get() = reuseId

    override fun register(intoView: UITableView) {
        intoView.registerClass(
            cellClass = UITableViewCell().`class`(),
            forCellReuseIdentifier = reusableIdentifier
        )
    }

    override fun bind(cell: UITableViewCell) {
        val liveData = cell.getWidgetLiveData<T>()
        if (liveData != null) {
            liveData.value = data
        } else {
            val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController!!
            val mutableLiveData = MutableLiveData(initialValue = data)
            val widget = createWidget(mutableLiveData)
            val view = widget.buildView(viewFactoryContext = viewController).apply {
                translatesAutoresizingMaskIntoConstraints = false
            }

            with(cell.contentView) {
                addSubview(view)

                view.topAnchor.constraintEqualToAnchor(topAnchor).active = true
                view.leftAnchor.constraintEqualToAnchor(leftAnchor).active = true
                view.rightAnchor.constraintEqualToAnchor(rightAnchor).active = true
                view.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
            }

            cell.setWidgetLiveData(mutableLiveData)
        }
    }
}

private fun <T> UITableViewCell.getWidgetLiveData(): MutableLiveData<T>? {
    return getAssociatedObject(this) as? MutableLiveData<T>
}

private fun <T> UITableViewCell.setWidgetLiveData(liveData: MutableLiveData<T>) {
    setAssociatedObject(this, liveData)
}