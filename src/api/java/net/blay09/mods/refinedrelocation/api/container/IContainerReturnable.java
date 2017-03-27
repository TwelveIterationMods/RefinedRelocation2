package net.blay09.mods.refinedrelocation.api.container;

import javax.annotation.Nullable;

public interface IContainerReturnable {
	void setReturnCallback(@Nullable ReturnCallback callback);
	@Nullable ReturnCallback getReturnCallback();
}
