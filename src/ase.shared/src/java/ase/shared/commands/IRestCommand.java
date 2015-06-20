package ase.shared.commands;

import java.util.Collection;

/**
 * Created by Michael on 20.06.2015.
 */
public interface IRestCommand<TReturn> {

    Collection<TReturn> execute();
}
