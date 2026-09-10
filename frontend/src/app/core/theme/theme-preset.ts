import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

/**
 * Tema "Cinema" — superfícies escuras profundas com acento vermelho de sala de projeção.
 * O preset alimenta os componentes PrimeNG; os tokens crus vivem em styles.scss.
 */
export const CatalogoPreset = definePreset(Aura, {
  primitive: {
    borderRadius: {
      none: '0',
      xs: '0.25rem',
      sm: '0.375rem',
      md: '0.5rem',
      lg: '0.75rem',
      xl: '1rem',
    },
  },
  semantic: {
    primary: {
      50: '#fff1f2',
      100: '#ffe0e2',
      200: '#ffc6ca',
      300: '#ff9ba3',
      400: '#ff616d',
      500: '#f5313f',
      600: '#e50914',
      700: '#c00610',
      800: '#9d0a12',
      900: '#820f16',
      950: '#470206',
    },
    focusRing: {
      width: '2px',
      style: 'solid',
      color: '{primary.500}',
      offset: '2px',
    },
    formField: {
      paddingX: '0.75rem',
      paddingY: '0.6rem',
      borderRadius: '{border.radius.md}',
      transitionDuration: '0.15s',
    },
    colorScheme: {
      light: {
        surface: {
          0: '#ffffff',
          50: '#f7f7f8',
          100: '#eeeef0',
          200: '#e2e2e5',
          300: '#cbcbd1',
          400: '#9d9da6',
          500: '#71717a',
          600: '#52525b',
          700: '#3f3f46',
          800: '#27272a',
          900: '#18181b',
          950: '#09090b',
        },
      },
      dark: {
        surface: {
          0: '#ffffff',
          50: '#f4f4f6',
          100: '#e4e4e9',
          200: '#c7c7d1',
          300: '#9a9aa8',
          400: '#6e6e80',
          500: '#4d4d5c',
          600: '#3a3a47',
          700: '#2a2a36',
          800: '#1c1c26',
          900: '#13131b',
          950: '#0b0b11',
        },
        content: {
          background: '#13131b',
          borderColor: 'rgba(255, 255, 255, 0.08)',
        },
        overlay: {
          modal: { background: '#161620', borderColor: 'rgba(255, 255, 255, 0.1)' },
          popover: { background: '#161620', borderColor: 'rgba(255, 255, 255, 0.1)' },
          select: { background: '#161620', borderColor: 'rgba(255, 255, 255, 0.1)' },
        },
        formField: {
          background: 'rgba(255, 255, 255, 0.04)',
          borderColor: 'rgba(255, 255, 255, 0.1)',
          hoverBorderColor: 'rgba(255, 255, 255, 0.22)',
          focusBorderColor: '{primary.500}',
          color: '#f4f4f6',
          placeholderColor: '#6e6e80',
        },
        text: {
          color: '#ececf1',
          mutedColor: '#8f8fa3',
        },
      },
    },
  },
});
