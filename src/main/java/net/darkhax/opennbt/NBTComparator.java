package net.darkhax.opennbt;

import net.darkhax.opennbt.tags.Tag;

import java.util.Comparator;

public final class NBTComparator implements Comparator<Tag> {

    @Override
    public int compare(Tag firstTag, Tag secondTag) {

        return firstTag != null && !firstTag.equals(secondTag) ? 1 : secondTag != null ? -1 : 0;
    }
}