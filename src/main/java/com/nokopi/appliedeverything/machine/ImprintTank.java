package com.nokopi.appliedeverything.machine;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.nokopi.appliedeverything.imprint.ImprintRules;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKeyType;
import appeng.helpers.externalstorage.GenericStackInv;

/** The imprinter's fluid tank: one imprintable fluid, exactly enough for one imprint. */
public class ImprintTank extends GenericStackInv {
    public static final int CAPACITY = 1000;

    public ImprintTank(@Nullable Runnable listener) {
        super(Set.of(AEKeyType.fluids()), listener, Mode.STORAGE, 1);
        setCapacity(AEKeyType.fluids(), CAPACITY);
        setFilter((slot, what) -> what instanceof AEFluidKey fluid && ImprintRules.canImprint(fluid));
    }

    @Nullable
    public AEFluidKey getFluid() {
        return getKey(0) instanceof AEFluidKey fluid ? fluid : null;
    }

    public boolean isFull() {
        return getAmount(0) >= CAPACITY;
    }
}
