package com.nexuszen.auth.models.enums;

public enum EstadoUsuario {
    ACTIVO {
        @Override
        public EstadoUsuario suspender() { return SUSPENDIDO; }
        @Override
        public EstadoUsuario darDeBaja() { return INACTIVO; }
        @Override
        public boolean puedeLoguear() { return true; }
    },
    INACTIVO {
        @Override
        public EstadoUsuario reactivar() { return ACTIVO; }
        @Override
        public boolean puedeLoguear() { return false; }
    },
    SUSPENDIDO {
        @Override
        public EstadoUsuario reactivar() { return ACTIVO; }
        @Override
        public EstadoUsuario darDeBaja() { return INACTIVO; }
        @Override
        public boolean puedeLoguear() { return false; }
    },
    PENDIENTE {
        @Override
        public EstadoUsuario activar() { return ACTIVO; }
        @Override
        public EstadoUsuario darDeBaja() { return INACTIVO; }
        @Override
        public boolean puedeLoguear() { return false; }
    };

    public EstadoUsuario activar() { throw new IllegalStateException("Transición no permitida desde estado " + this.name()); }
    public EstadoUsuario suspender() { throw new IllegalStateException("Transición no permitida desde estado " + this.name()); }
    public EstadoUsuario darDeBaja() { throw new IllegalStateException("Transición no permitida desde estado " + this.name()); }
    public EstadoUsuario reactivar() { throw new IllegalStateException("Transición no permitida desde estado " + this.name()); }
    
    public abstract boolean puedeLoguear();
}
