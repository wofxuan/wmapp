// filename: CallbackBundle.java
package com.mx.android.wmapp.common;

import android.os.Bundle;

import java.io.IOException;
// 简单的Bundle参数回调接口
public interface CallbackBundle {
	abstract void callback(Bundle bundle) throws IOException;
}
