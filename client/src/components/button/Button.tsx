import styles from "./Button.module.css";

interface Props {
    text: String;
    handleClick: React.MouseEventHandler<HTMLButtonElement>;
    className?: String;
}

export const Button = ({ text, handleClick, className }: Props) => {
    return (
        <button 
            onClick={handleClick} 
            className={`${styles.button} ${className}`}
        >
            {text}
        </button>
    );
}