package net.blay09.mods.refinedrelocation.api.container;

import javax.annotation.Nullable;

public interface IHasReturnCallback {
	void setReturnCallback(@Nullable ReturnCallback callback);
	@Nullable ReturnCallback getReturnCallback();
}
